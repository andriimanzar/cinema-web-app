package com.manzar.controller.servlet.adminPanel.movie_managing;

import com.manzar.persistence.dao.MovieDao;
import com.manzar.persistence.dao.impl.MovieDaoImpl;
import com.manzar.persistence.entity.Movie;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/addMovie")
public class AddMovieServlet extends HttpServlet {

    private MovieDao movieDao;

    @Override
    public void init() throws ServletException {
        movieDao = MovieDaoImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/add-movie.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        saveMovie(req);
        resp.sendRedirect("/manageMovies");
    }

    private void saveMovie(HttpServletRequest request) {
        Movie movie = MovieRequestHelper.parseMovieFromRequest(request);
        movieDao.save(movie);
    }
}
