package com.manzar.persistence.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class MovieSession {

    private Long movieId;
    private LocalDateTime showTime;

    public MovieSession() {
    }

    public MovieSession(Long movieId, LocalDateTime showTime) {
        this.movieId = movieId;
        this.showTime = showTime;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public LocalDateTime getShowTime() {
        return showTime;
    }

    public void setShowTime(LocalDateTime showTime) {
        this.showTime = showTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieSession that = (MovieSession) o;
        return Objects.equals(movieId, that.movieId) && Objects.equals(showTime, that.showTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, showTime);
    }

    @Override
    public String toString() {
        return "MovieSession{" +
                "movieId=" + movieId +
                ", showTime=" + showTime +
                '}';
    }
}
