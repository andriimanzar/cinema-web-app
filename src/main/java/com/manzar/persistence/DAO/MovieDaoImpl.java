package com.manzar.persistence.DAO;

import com.manzar.persistence.entity.Movie;
import com.manzar.persistence.exception.DBException;
import com.manzar.util.DBConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.manzar.persistence.DAO.SQLQueries.MovieQuery.*;

public class MovieDaoImpl implements MovieDao {

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
        try (Connection connection = DBConnector.getConnection()) {
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
            PreparedStatement insertStatement = connection.prepareStatement(INSERT_MOVIE_SQL,
                    Statement.RETURN_GENERATED_KEYS);
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
    }

    @Override
    public List<Movie> findAll() {
        try (Connection connection = DBConnector.getConnection()) {
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
        movie.setId(resultSet.getLong("id"));
        return movie;
    }


    @Override
    public Movie findMovie(Long id) {
        Objects.requireNonNull(id);
        try (Connection connection = DBConnector.getConnection()) {
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
        try (Connection connection = DBConnector.getConnection()) {
            updateMovie(movie, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot update movie: %s", movie), e);
        }
    }

    private void updateMovie(Movie movie, Connection connection) throws SQLException {
        checkIdIsNotNull(movie);
        PreparedStatement updateStatement = prepareUpdateStatement(movie, connection);
        updateStatement.executeUpdate();
    }

    private PreparedStatement prepareUpdateStatement(Movie movie, Connection connection) {

        try {
            PreparedStatement updateStatement = connection.prepareStatement(UPDATE_MOVIE_SQL);
            fillMovieStatement(movie, updateStatement);
            updateStatement.setLong(6, movie.getId());
            return updateStatement;
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot prepare update statement for movie: %s", movie));
        }
    }

    @Override
    public void remove(Movie movie) {
        Objects.requireNonNull(movie);
        try (Connection connection = DBConnector.getConnection()) {
            removeMovie(movie, connection);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot remove movie: %s", movie), e);
        }
    }

    private void removeMovie(Movie movie, Connection connection) throws SQLException {
        checkIdIsNotNull(movie);
        PreparedStatement removeStatement = prepareRemoveStatement(movie, connection);
        removeStatement.executeUpdate();
    }

    private PreparedStatement prepareRemoveStatement(Movie movie, Connection connection) {
        try {
            PreparedStatement removeStatement = connection.prepareStatement(DELETE_MOVIE_SQL);
            removeStatement.setLong(1, movie.getId());
            return removeStatement;
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot prepare remove statement for movie: %s", movie), e);
        }
    }

    private void checkIdIsNotNull(Movie movie) {
        if (movie.getId() == null) {
            throw new DBException("Movie id cannot be null");
        }
    }
}