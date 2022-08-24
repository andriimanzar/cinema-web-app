package com.manzar.persistence.entity;

public class Movie {

    private Long id;

    private String title;

    private String genre;

    private Long duration;

    private String director;

    private int releaseYear;

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", duration=" + duration +
                ", director='" + director + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }

    public Movie(String title, String genre, Long duration, String director, int releaseYear) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.director = director;
        this.releaseYear = releaseYear;
    }

    public Movie() {
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
}
