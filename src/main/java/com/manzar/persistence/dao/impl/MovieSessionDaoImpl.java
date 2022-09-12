package com.manzar.persistence.dao.impl;

import com.manzar.persistence.dao.MovieSessionDao;
import com.manzar.persistence.entity.MovieSession;
import com.manzar.persistence.exception.DBException;
import com.manzar.util.DBConnector;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class MovieSessionDaoImpl implements MovieSessionDao {

    public static final String INSERT_MOVIE_SESSION_SQL = "INSERT INTO movie_session(movie_id, show_time) " +
            "VALUES(?,?)";

    public static final String SELECT_MOVIE_SESSION_MOVIE_SQL = "SELECT show_time, title from movie_session " +
            "INNER JOIN movies ON movie_id = id";

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
    public MovieSession findByDateAndTime(LocalDateTime localDateTime) {
        return null;
    }

    @Override
    public Map<Timestamp, String> findAllMovieSessions() {
        try (Connection connection = DBConnector.getConnection()) {
            return getAllMoviesSession(connection);
        } catch (SQLException e) {
            throw new DBException("Cannot find all movie sessions");
        }
    }

    private Map<Timestamp, String> getAllMoviesSession(Connection connection) throws SQLException {
        Statement getAllStatement = connection.createStatement();
        ResultSet resultSet = getAllStatement.executeQuery(SELECT_MOVIE_SESSION_MOVIE_SQL);
        return parseResultToMap(resultSet);
    }

    private Map<Timestamp, String> parseResultToMap(ResultSet resultSet) throws SQLException {
        Map<Timestamp, String> showTimeMovieMap = new HashMap<>();
        while (resultSet.next()) {
            showTimeMovieMap.put(resultSet.getTimestamp("show_time"),
                    resultSet.getString("title"));
        }
        return showTimeMovieMap;
    }

}
