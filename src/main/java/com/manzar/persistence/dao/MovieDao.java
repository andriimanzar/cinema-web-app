package com.manzar.persistence.dao;

import com.manzar.persistence.entity.Movie;

import java.util.List;

public interface MovieDao {

    void save(Movie movie);

    List<Movie> findAll();

    List<Movie> findAllMoviesWithLimit(int pageSize, int offset);

    Movie findMovie(Long id);

    void update(Movie movie);

    void remove(Long id);

    long countAllDatabaseRows();
}
