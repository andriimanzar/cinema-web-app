package com.manzar.persistence.dao;

import com.manzar.persistence.entity.Seat;

import java.util.List;

public interface SeatDao {

    List<Seat> findAllUnreservedSeats(Long movieSessionId);

    List<Seat> findAllReservedSeats(Long movieSessionId);
}
