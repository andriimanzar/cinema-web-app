package com.manzar.controller.servlet.adminPanel.movie_managing;

import com.manzar.controller.servlet.RequestParser;
import com.manzar.persistence.dao.MovieDao;
import com.manzar.persistence.dao.impl.MovieDaoImpl;
import com.manzar.persistence.entity.Movie;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/editMovie/*")
public class EditMovieServlet extends HttpServlet {

    private MovieDao movieDao;

    @Override
    public void init() {
        movieDao = MovieDaoImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestParser.parseIdFromRequest(req);
        Movie movieFromDb = movieDao.findMovie(id);
        MovieRequestHelper.sendMovieThroughRequest(req, movieFromDb);
        req.getRequestDispatcher("/WEB-INF/edit-movie.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = RequestParser.parseIdFromRequest(req);
        parseMovieFromRequestAndUpdate(req, id);
        resp.sendRedirect("/manageMovies");
    }

    private void parseMovieFromRequestAndUpdate(HttpServletRequest request, Long id) {
        Movie parsedMovie = MovieRequestHelper.parseMovieFromRequest(request);
        parsedMovie.setId(id);
        movieDao.update(parsedMovie);
    }
}
