package com.manzar.persistence.DAO;

import com.manzar.persistence.entity.Movie;

import java.util.List;

public interface MovieDao {

    void save(Movie movie);

    List<Movie> findAll();

    Movie findMovie(Long id);

    void update(Movie movie);

    void remove(Movie movie);
}
