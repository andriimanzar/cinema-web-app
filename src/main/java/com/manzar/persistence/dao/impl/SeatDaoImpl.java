package com.manzar.persistence.dao.impl;

import com.manzar.persistence.DBConnection;
import com.manzar.persistence.dao.SeatDao;
import com.manzar.persistence.entity.Seat;
import com.manzar.persistence.exception.DBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDaoImpl implements SeatDao {

    public static final String SELECT_ALL_SEATS_SQL = "SELECT * from seats WHERE is_reserved = ?";
    private static SeatDao seatDao;

    private SeatDaoImpl() {

    }

    public static synchronized SeatDao getInstance() {
        if (seatDao == null) {
            seatDao = new SeatDaoImpl();
        }
        return seatDao;
    }

    @Override
    public List<Seat> findAllUnreservedSeats(Long movieSessionId) {
        try (Connection connection = DBConnection.getConnection()) {
            return findAllSeats(connection, Boolean.FALSE);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot find all unreserved seats to movieSessionId : %d", movieSessionId), e);
        }
    }

    @Override
    public List<Seat> findAllReservedSeats(Long movieSessionId) {
        try (Connection connection = DBConnection.getConnection()) {
            return findAllSeats(connection, Boolean.TRUE);
        } catch (SQLException e) {
            throw new DBException(String.format("Cannot find all reserved seats to movieSessionId : %d", movieSessionId), e);
        }
    }

    private List<Seat> findAllSeats(Connection connection, boolean isReserved) throws SQLException {
        PreparedStatement findAllSeatsStatement = connection.prepareStatement(SELECT_ALL_SEATS_SQL);
        findAllSeatsStatement.setBoolean(1, isReserved);
        ResultSet resultSet = findAllSeatsStatement.executeQuery();
        return parseResultSet(resultSet);
    }

    private List<Seat> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Seat> seatList = new ArrayList<>();
        while (resultSet.next()) {
            Seat seat = parseSeat(resultSet);
            seatList.add(seat);
        }
        return seatList;
    }

    private Seat parseSeat(ResultSet resultSet) throws SQLException {
        Seat seat = new Seat();
        seat.setRowNumber(resultSet.getInt("seat_number"));
        seat.setSeatNumber(resultSet.getInt("row_number"));
        seat.setReserved(resultSet.getBoolean("is_reserved"));
        seat.setMovieSessionId(resultSet.getLong("movie_session_id"));
        return seat;
    }
}
