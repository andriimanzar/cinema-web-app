package com.manzar.persistence.dao.impl;

import com.manzar.persistence.DBConnection;
import com.manzar.persistence.dao.MovieDao;
import com.manzar.persistence.entity.Movie;
import com.manzar.persistence.exception.DBException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieDaoImpl implements MovieDao {

    public static final String INSERT_MOVIE_SQL = "INSERT INTO movies" + "(title, genre, duration, director, release_year, imageURL) VALUES(?,?,?,?,?,?,?)";
    public static final String SELECT_ALL_MOVIES_SQL = "SELECT * from movies";
    public static final String SELECT_ALL_MOVIES_WITH_LIMIT = "SELECT * from movies LIMIT ? OFFSET ?";
    public static final String SELECT_ALL_MOVIES_WITH_CONCRETE_GENRE_SQL = "SELECT * from movies WHERE genre = ?";
    public static final String SELECT_MOVIE_BY_ID_SQL = "SELECT * from movies WHERE id = ?";
    public static final String UPDATE_MOVIE_SQL = "UPDATE movies SET title = ?, genre = ?, " + "duration = ?, director = ?, release_year = ?, imageURL = ?, trailerURL = ?  WHERE id = ?";
    public static final String DELETE_MOVIE_SQL = "DELETE from movies WHERE id = ?";
    public static final String COUNT_ROWS_SQL = "SELECT count(*) from movies";
    public static final String FIND_ALL_GENRES = "SELECT distinct genre from movies";


    private static MovieDao movieDao;

    private MovieDaoImpl() {

    }

    public static synchronized MovieDao getInstance() {
        if (movieDao == null) {
            movieDao = new MovieDaoImpl();
        }
        return movieDao;
    }

    @Override
    public void save(Movie movie) {
        Objects.requireNonNull(movie);
        try (Connection connection = DBConnection.getConnection()) {
            saveMovie(movie, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot save movie: %s", movie), e);
        }
    }

    private void saveMovie(Movie movie, Connection connection) throws SQLException {
        PreparedStatement insertStatement = prepareInsertStatement(movie, connection);
        insertStatement.executeUpdate();
        Long generatedId = fetchGeneratedId(insertStatement);
        movie.setId(generatedId);
    }

    private PreparedStatement prepareInsertStatement(Movie movie, Connection connection) {
        try {
            PreparedStatement insertStatement = connection.prepareStatement(INSERT_MOVIE_SQL, Statement.RETURN_GENERATED_KEYS);
            fillMovieStatement(movie, insertStatement);
            return insertStatement;
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot prepare statement for movie : %s", movie), e);
        }

    }

    private Long fetchGeneratedId(PreparedStatement preparedStatement) throws SQLException {
        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getLong("id");
        } else throw new DBException("Cannot fetch generated id");
    }

    private void fillMovieStatement(Movie movie, PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(1, movie.getTitle());
        insertStatement.setString(2, movie.getGenre());
        insertStatement.setLong(3, movie.getDuration());
        insertStatement.setString(4, movie.getDirector());
        insertStatement.setInt(5, movie.getReleaseYear());
        insertStatement.setString(6, movie.getImageURL());
        insertStatement.setString(7, movie.getTrailerURL());
    }

    @Override
    public List<Movie> findAll() {
        try (Connection connection = DBConnection.getConnection()) {
            return getAllMovies(connection);
        } catch (SQLException e) {
            throw new DBException("Cannot find all movies");
        }
    }

    private List<Movie> getAllMovies(Connection connection) throws SQLException {
        Statement getAllStatement = connection.createStatement();
        ResultSet resultSet = getAllStatement.executeQuery(SELECT_ALL_MOVIES_SQL);
        return collectToList(resultSet);
    }

    private List<Movie> collectToList(ResultSet resultSet) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        while (resultSet.next()) {
            Movie movie = parseRow(resultSet);
            movies.add(movie);
        }
        return movies;
    }

    private Movie parseRow(ResultSet resultSet) throws SQLException {
        Movie movie = new Movie();
        movie.setTitle(resultSet.getString("title"));
        movie.setGenre(resultSet.getString("genre"));
        movie.setDuration(resultSet.getLong("duration"));
        movie.setDirector(resultSet.getString("director"));
        movie.setReleaseYear(resultSet.getInt("release_year"));
        movie.setImageURL(resultSet.getString("imageURL"));
        movie.setTrailerURL(resultSet.getString("trailerURL"));
        movie.setId(resultSet.getLong("id"));
        return movie;
    }

    public List<Movie> findAllMoviesWithLimit(int pageSize, int offset) {
        try (Connection connection = DBConnection.getConnection()) {
            return getAllMoviesWithLimit(connection, pageSize, offset);
        } catch (SQLException e) {
            throw new DBException("Cannot find all movies with limit");
        }
    }

    private List<Movie> getAllMoviesWithLimit(Connection connection, int pageSize, int offset)
            throws SQLException {
        PreparedStatement selectAllWithLimitStatement = prepareSelectAllWithLimitStatement(connection, pageSize, offset);
        ResultSet resultSet = selectAllWithLimitStatement.executeQuery();
        return collectToList(resultSet);

    }

    private PreparedStatement prepareSelectAllWithLimitStatement(Connection connection, int pageSize, int offset)
            throws SQLException {
        PreparedStatement selectAllWithLimitStatement = connection.prepareStatement(SELECT_ALL_MOVIES_WITH_LIMIT);
        selectAllWithLimitStatement.setInt(1, pageSize);
        selectAllWithLimitStatement.setInt(2, offset);
        return selectAllWithLimitStatement;
    }

    @Override
    public List<Movie> findAllMoviesByGenre(String genre) {
        Objects.requireNonNull(genre);
        try (Connection connection = DBConnection.getConnection()) {
            return getAllMoviesByGenre(connection, genre);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot find all movies with genre %s", genre), e);
        }
    }

    private List<Movie> getAllMoviesByGenre(Connection connection, String genre) throws SQLException {
        PreparedStatement selectByGenreStatement = prepareSelectByGenreStatement(connection, genre);
        ResultSet resultSet = selectByGenreStatement.executeQuery();
        return collectToList(resultSet);
    }

    private PreparedStatement prepareSelectByGenreStatement(Connection connection, String genre) throws SQLException {
        PreparedStatement selectByGenreStatement = connection.prepareStatement(SELECT_ALL_MOVIES_WITH_CONCRETE_GENRE_SQL);
        selectByGenreStatement.setString(1, genre);
        return selectByGenreStatement;
    }

    @Override
    public Movie findMovie(Long id) {
        Objects.requireNonNull(id);
        try (Connection connection = DBConnection.getConnection()) {
            return findById(id, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot find movie with id: %d", id));
        }
    }

    private Movie findById(Long id, Connection connection) throws SQLException {
        PreparedStatement selectByIdStatement = prepareSelectByIdStatement(id, connection);
        ResultSet resultSet = selectByIdStatement.executeQuery();
        if (resultSet.next()) {
            return parseRow(resultSet);
        } else throw new DBException(String.format("Movie with id = %d does not exist", id));

    }

    private PreparedStatement prepareSelectByIdStatement(Long id, Connection connection) {
        try {
            PreparedStatement selectByIdStatement = connection.prepareStatement(SELECT_MOVIE_BY_ID_SQL);
            selectByIdStatement.setLong(1, id);
            return selectByIdStatement;
        } catch (SQLException e) {
            throw new DBException("Cannot prepare select by id statement");
        }
    }

    @Override
    public void update(Movie movie) {
        Objects.requireNonNull(movie);
        try (Connection connection = DBConnection.getConnection()) {
            updateMovie(movie, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot update movie: %s", movie), e);
        }
    }

    private void updateMovie(Movie movie, Connection connection) throws SQLException {
        checkId(movie.getId());
        PreparedStatement updateStatement = prepareUpdateStatement(movie, connection);
        updateStatement.executeUpdate();
    }

    private PreparedStatement prepareUpdateStatement(Movie movie, Connection connection) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_MOVIE_SQL);
            fillMovieStatement(movie, updateStatement);
            updateStatement.setLong(8, movie.getId());
            return updateStatement;
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot prepare update statement for movie: %s", movie));
        }
    }

    @Override
    public void remove(Long id) {
        try (Connection connection = DBConnection.getConnection()) {
            removeMovie(id, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot remove movie with id: %d", id), e);
        }
    }

    private void removeMovie(Long id, Connection connection) throws SQLException {
        checkId(id);
        PreparedStatement removeStatement = prepareRemoveStatement(id, connection);
        removeStatement.executeUpdate();
    }

    private PreparedStatement prepareRemoveStatement(Long id, Connection connection) {
        try {
            PreparedStatement removeStatement = connection.prepareStatement(DELETE_MOVIE_SQL);
            removeStatement.setLong(1, id);
            return removeStatement;
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot prepare remove statement for movie with id: %d", id), e);
        }
    }

    private void checkId(Long id) {
        if (id == null || id < 1) {
            throw new DBException("Movie id cannot be null or less than 1");
        }
    }

    public long countAllDatabaseRows() {
        try (Connection connection = DBConnection.getConnection()) {
            return executeAndParseCountStatement(connection);
        } catch (SQLException e) {
            throw new DBException("Cannot count database rows!");
        }
    }

    private long executeAndParseCountStatement(Connection connection) throws SQLException {
        Statement countStatement = connection.createStatement();
        ResultSet resultSet = countStatement.executeQuery(COUNT_ROWS_SQL);
        if (resultSet.next()) {
            return resultSet.getLong("count");
        } else throw new DBException("Cannot count database rows");
    }

    @Override
    public List<String> findAllGenres() {
        try (Connection connection = DBConnection.getConnection()) {
           return findGenres(connection);
        } catch (SQLException e) {
            throw new DBException("Cannot find all genres");
        }
    }

    private List<String> findGenres(Connection connection) throws SQLException {
        Statement selectAllGenresStatement = connection.createStatement();
        ResultSet resultSet = selectAllGenresStatement.executeQuery(FIND_ALL_GENRES);
        return parseGenresResultSet(resultSet);
    }

    private List<String> parseGenresResultSet(ResultSet resultSet) throws SQLException {
        List<String> genres = new ArrayList<>();
        while (resultSet.next()) {
            String genre = resultSet.getString("genre");
            genres.add(genre);
        }
        return genres;
    }

}
