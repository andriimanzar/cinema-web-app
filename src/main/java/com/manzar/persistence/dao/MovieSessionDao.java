package com.manzar.persistence.dao;

import com.manzar.persistence.entity.MovieSession;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public interface MovieSessionDao {

    void save(MovieSession movieSession);

    MovieSession findByDateAndTime(LocalDateTime localDateTime);

    Map<Timestamp, String> findAllMovieSessions();
}
