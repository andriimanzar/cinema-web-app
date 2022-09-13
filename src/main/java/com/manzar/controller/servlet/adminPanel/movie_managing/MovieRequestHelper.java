package com.manzar.controller.servlet.adminPanel.movie_managing;

import com.manzar.persistence.entity.Movie;
import jakarta.servlet.http.HttpServletRequest;

public class MovieRequestHelper {

    private MovieRequestHelper() {
    }

    public static Movie parseMovieFromRequest(HttpServletRequest request) {
        String title = request.getParameter("movieTitle");
        String genre = request.getParameter("movieGenre");
        Long duration = Long.valueOf(request.getParameter("duration"));
        String producer = request.getParameter("director");
        int releaseYear = Integer.parseInt(request.getParameter("releaseYear"));
        String imageUrl = request.getParameter("imageUrl");
        String trailerURL = request.getParameter("trailerURL");
        return new Movie(title, genre, duration, producer, releaseYear, imageUrl, trailerURL);
    }

    public static void sendMovieThroughRequest(HttpServletRequest request, Movie movie) {
        request.setAttribute("movieTitleFromDb", movie.getTitle());
        request.setAttribute("movieGenreFromDb", movie.getGenre());
        request.setAttribute("movieDurationFromDb", movie.getDuration());
        request.setAttribute("movieDirectorFromDb", movie.getDirector());
        request.setAttribute("releaseYearFromDb", movie.getReleaseYear());
        request.setAttribute("imageURL", movie.getImageURL());
        request.setAttribute("trailerURL", movie.getTrailerURL());
        request.setAttribute("movieId", movie.getId());
    }


}
