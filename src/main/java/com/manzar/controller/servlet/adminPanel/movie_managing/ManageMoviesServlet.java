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
import java.util.List;

@WebServlet("/manageMovies")
public class ManageMoviesServlet extends HttpServlet {

    private MovieDao movieDao;

    @Override
    public void init() {
        movieDao = MovieDaoImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Movie> movieList = movieDao.findAll();
        req.setAttribute("movieList", movieList);
        req.getRequestDispatcher("/WEB-INF/manage-movies.jsp").forward(req, resp);
    }
}
