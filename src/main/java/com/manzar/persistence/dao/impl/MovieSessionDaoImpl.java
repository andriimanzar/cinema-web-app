package com.manzar.persistence.dao.impl;

import com.manzar.persistence.DBConnection;
import com.manzar.persistence.dao.MovieSessionDao;
import com.manzar.persistence.entity.MovieSession;
import com.manzar.persistence.exception.DBException;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MovieSessionDaoImpl implements MovieSessionDao {

    public static final String INSERT_MOVIE_SESSION_SQL = "INSERT INTO movie_session(movie_id, start_time, end_time, ticket_price) " +
            "VALUES(?,?,?,?)";

    public static final String SELECT_MOVIE_SESSION_BY_ID_SQL = "SELECT * from movie_session WHERE id = ?";

    public static final String DELETE_MOVIE_SESSION_SQL = "DELETE from movie_session WHERE id = ?";

    public static final String UPDATE_MOVIE_SESSION_SQL = "UPDATE movie_session SET movie_id = ?, " +
            "start_time = ?, end_time = ?, ticket_price = ? WHERE id = ?";

    public static final String SELECT_MOVIE_SESSION_MOVIE_SQL = "SELECT movie_session.id, start_time, end_time, movie_id, ticket_price, title from movie_session " +
            "INNER JOIN movies ON movie_id = movies.id";

    public static final String FIND_CLOSEST_SESSIONS_SQL = "SELECT * from movie_session WHERE movie_id = ? ORDER BY start_time";

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
        try (Connection connection = DBConnection.getConnection()) {
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
        insertStatement.setTimestamp(2, Timestamp.valueOf(movieSession.getStartTime()));
        insertStatement.setTimestamp(3, Timestamp.valueOf(movieSession.getEndTime()));
        insertStatement.setBigDecimal(4, movieSession.getTicketPrice());
        return insertStatement;
    }

    @Override
    public MovieSession findById(Long id) {
        try (Connection connection = DBConnection.getConnection()) {
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
        MovieSession movieSession = new MovieSession();
        movieSession.setId(resultSet.getLong("id"));
        movieSession.setMovieId(resultSet.getLong("movie_id"));
        movieSession.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
        movieSession.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
        movieSession.setTicketPrice(resultSet.getBigDecimal("ticket_price"));
        return movieSession;
    }

    private PreparedStatement prepareSelectByIdStatement(Connection connection, Long id) throws SQLException {
        PreparedStatement selectByIdStatement = connection.prepareStatement(SELECT_MOVIE_SESSION_BY_ID_SQL);
        selectByIdStatement.setLong(1, id);
        return selectByIdStatement;
    }

    @Override
    public void remove(Long id) {
        try (Connection connection = DBConnection.getConnection()) {
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
        try (Connection connection = DBConnection.getConnection()) {
            prepareAndExecuteUpdateStatement(connection, movieSession);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot update movie session: %s", movieSession), e);
        }
    }


    private void prepareAndExecuteUpdateStatement(Connection connection, MovieSession movieSession) throws SQLException {
        PreparedStatement updateStatement = connection.prepareStatement(UPDATE_MOVIE_SESSION_SQL);
        updateStatement.setLong(1, movieSession.getMovieId());
        updateStatement.setTimestamp(2, Timestamp.valueOf(movieSession.getStartTime()));
        updateStatement.setTimestamp(3, Timestamp.valueOf(movieSession.getEndTime()));
        updateStatement.setLong(4, movieSession.getId());
        updateStatement.setBigDecimal(5, movieSession.getTicketPrice());
        updateStatement.executeUpdate();
    }


    @Override
    public MovieSession findByDateAndTime(LocalDateTime localDateTime) {
        return null;
    }

    @Override
    public Map<MovieSession, String> findAllMovieSessions() {
        try (Connection connection = DBConnection.getConnection()) {
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
            LocalDateTime startTime = resultSet.getTimestamp("start_time").toLocalDateTime();
            LocalDateTime endTime = resultSet.getTimestamp("end_time").toLocalDateTime();
            Long id = resultSet.getLong("id");
            BigDecimal ticketPrice = resultSet.getBigDecimal("ticket_price");
            String movieTitle = resultSet.getString("title");
            MovieSession movieSession = new MovieSession(movieId, startTime, endTime, ticketPrice);
            movieSession.setId(id);
            showTimeMovieMap.put(movieSession, movieTitle);
        }
        return showTimeMovieMap;
    }

    @Override
    public List<MovieSession> findClosestMovieSessions(Long movieId) {
        try (Connection connection = DBConnection.getConnection()) {
            return findClosest(connection, movieId);
        } catch (SQLException e) {
            throw new DBException("Cannot find closest movie sessions", e);
        }
    }

    private List<MovieSession> findClosest(Connection connection, Long movieId) throws SQLException {
        PreparedStatement selectClosestSessionsStatement = prepareSelectClosestSessionsStatement(connection, movieId);
        ResultSet resultSet = selectClosestSessionsStatement.executeQuery();
        return collectToList(resultSet);
    }

    private List<MovieSession> collectToList(ResultSet resultSet) throws SQLException {
        List<MovieSession> movieSessionsList = new ArrayList<>();
        while (resultSet.next()) {
            MovieSession movieSession = parseResultSet(resultSet);
            movieSessionsList.add(movieSession);
        }
        return movieSessionsList;
    }

    private PreparedStatement prepareSelectClosestSessionsStatement(Connection connection, Long movieId) throws SQLException {
        PreparedStatement selectClosestSessionsStatement = connection.prepareStatement(FIND_CLOSEST_SESSIONS_SQL);
        selectClosestSessionsStatement.setLong(1, movieId);
        return selectClosestSessionsStatement;
    }
}
