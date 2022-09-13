package com.manzar.persistence.dao.impl;

import com.manzar.persistence.dao.MovieSessionDao;
import com.manzar.persistence.entity.MovieSession;
import com.manzar.persistence.exception.DBException;
import com.manzar.util.DBConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MovieSessionDaoImpl implements MovieSessionDao {

    public static final String INSERT_MOVIE_SESSION_SQL = "INSERT INTO movie_session(movie_id, show_time) " +
            "VALUES(?,?)";

    public static final String SELECT_MOVIE_SESSION_BY_ID_SQL = "SELECT * from movie_session WHERE id = ?";

    public static final String DELETE_MOVIE_SESSION_SQL = "DELETE from movie_session WHERE id = ?";

    public static final String UPDATE_MOVIE_SESSION_SQL = "UPDATE movie_session SET movie_id = ?, " +
            "show_time = ? WHERE id = ?";

    public static final String SELECT_MOVIE_SESSION_MOVIE_SQL = "SELECT movie_session.id, show_time, movie_id, title from movie_session " +
            "INNER JOIN movies ON movie_id = movies.id";

    private static MovieSessionDao movieSessionDao;

    private MovieSessionDaoImpl() {

    }

    public static synchronized MovieSessionDao getInstance() {
        if (movieSessionDao == null) {
            movieSessionDao = new MovieSessionDaoImpl();
        }
        return movieSessionDao;
    }

    @Override
    public void save(MovieSession movieSession) {
        try (Connection connection = DBConnector.getConnection()) {
            saveMovieSession(connection, movieSession);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot save movie session: %s", movieSession), e);
        }
    }

    private void saveMovieSession(Connection connection, MovieSession movieSession) throws SQLException {
        PreparedStatement insertStatement = prepareInsertStatement(connection, movieSession);
        insertStatement.execute();
    }

    private PreparedStatement prepareInsertStatement(Connection connection, MovieSession movieSession)
            throws SQLException {
        PreparedStatement insertStatement = connection.prepareStatement(INSERT_MOVIE_SESSION_SQL);
        insertStatement.setLong(1, movieSession.getMovieId());
        insertStatement.setTimestamp(2, Timestamp.valueOf(movieSession.getShowTime()));
        return insertStatement;
    }

    @Override
    public MovieSession findById(Long id) {
        try (Connection connection = DBConnector.getConnection()) {
            return findMovieSessionById(connection, id);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot find movie session with id %d", id), e);
        }
    }

    private MovieSession findMovieSessionById(Connection connection, Long id) throws SQLException {
        PreparedStatement selectByIdStatement = prepareSelectByIdStatement(connection, id);
        return parseResultSet(selectByIdStatement.executeQuery());
    }

    private MovieSession parseResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            MovieSession movieSession = new MovieSession();
            movieSession.setId(resultSet.getLong("id"));
            movieSession.setMovieId(resultSet.getLong("movie_id"));
            movieSession.setShowTime(resultSet.getTimestamp("show_time").toLocalDateTime());
            return movieSession;
        } else throw new DBException("Cannot find movie session");
    }

    private PreparedStatement prepareSelectByIdStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement selectByIdStatement = connection.prepareStatement(SELECT_MOVIE_SESSION_BY_ID_SQL);
        selectByIdStatement.setLong(1, id);
        return selectByIdStatement;
    }

    @Override
    public void remove(Long id) {
        try (Connection connection = DBConnector.getConnection()) {
            deleteSession(connection, id);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot remove session with id %d", id), e);
        }
    }

    private void deleteSession(Connection connection, Long id) throws SQLException {
        PreparedStatement deleteStatement = prepareDeleteStatement(connection, id);
        deleteStatement.execute();
    }

    private PreparedStatement prepareDeleteStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement deleteStatement = connection.prepareStatement(DELETE_MOVIE_SESSION_SQL);
        deleteStatement.setLong(1, id);
        return deleteStatement;
    }

    @Override
    public void update(MovieSession movieSession) {
        Objects.requireNonNull(movieSession);
        try (Connection connection = DBConnector.getConnection()) {
            prepareAndExecuteUpdateStatement(connection, movieSession);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot update movie session: %s", movieSession), e);
        }
    }


    private void prepareAndExecuteUpdateStatement(Connection connection, MovieSession movieSession) throws SQLException {
        PreparedStatement updateStatement = connection.prepareStatement(UPDATE_MOVIE_SESSION_SQL);
        updateStatement.setLong(1, movieSession.getMovieId());
        updateStatement.setTimestamp(2, Timestamp.valueOf(movieSession.getShowTime()));
        updateStatement.setLong(3, movieSession.getId());
        updateStatement.executeUpdate();
    }


    @Override
    public MovieSession findByDateAndTime(LocalDateTime localDateTime) {
        return null;
    }

    @Override
    public Map<MovieSession, String> findAllMovieSessions() {
        try (Connection connection = DBConnector.getConnection()) {
            return getAllMoviesSession(connection);
        } catch (SQLException e) {
            throw new DBException("Cannot find all movie sessions", e);
        }
    }

    private Map<MovieSession, String> getAllMoviesSession(Connection connection) throws SQLException {
        Statement getAllStatement = connection.createStatement();
        ResultSet resultSet = getAllStatement.executeQuery(SELECT_MOVIE_SESSION_MOVIE_SQL);
        return parseResultToMap(resultSet);
    }

    private Map<MovieSession, String> parseResultToMap(ResultSet resultSet) throws SQLException {
        Map<MovieSession, String> showTimeMovieMap = new HashMap<>();
        while (resultSet.next()) {
            Long movieId = resultSet.getLong("movie_id");
            LocalDateTime showTime = resultSet.getTimestamp("show_time").toLocalDateTime();
            Long id = resultSet.getLong("id");
            String movieTitle = resultSet.getString("title");
            MovieSession movieSession = new MovieSession(movieId, showTime);
            movieSession.setId(id);
            showTimeMovieMap.put(movieSession, movieTitle);
        }
        return showTimeMovieMap;
    }


}
