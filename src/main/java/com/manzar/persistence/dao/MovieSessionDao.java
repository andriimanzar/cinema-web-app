package com.manzar.persistence.dao;

import com.manzar.persistence.entity.MovieSession;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MovieSessionDao {

    void save(MovieSession movieSession);

    MovieSession findById(Long id);

    void remove(Long id);

    void update(MovieSession movieSession);

    MovieSession findByDateAndTime(LocalDateTime localDateTime);

    Map<MovieSession, String> findAllMovieSessions();

    List<MovieSession> findClosestMovieSessions(Long movieId);

}
