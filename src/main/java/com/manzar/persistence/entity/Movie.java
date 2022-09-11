package com.manzar.persistence.entity;

import java.util.Objects;

public class Movie {

    private Long id;
    private String title;
    private String genre;
    private Long duration;
    private String director;
    private int releaseYear;
    private String imageURL;
    private String trailerURL;

    @Override
    public String toString() {
        return "Movie{" + "id=" + id + ", title='" + title + '\'' + ", genre='" + genre + '\'' + ", duration=" + duration + ", director='" + director + '\'' + ", releaseYear=" + releaseYear + ", imageURL='" + imageURL + '\'' + ", trailerURL='" + trailerURL + '\'' + '}';
    }

    public Movie() {
    }
    public Movie(String title, String genre, Long duration, String director, int releaseYear, String imageURL, String trailerURL) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.director = director;
        this.releaseYear = releaseYear;
        this.imageURL = imageURL;
        this.trailerURL = trailerURL;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTrailerURL() {
        return trailerURL;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
