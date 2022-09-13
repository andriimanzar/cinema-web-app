package com.manzar.controller.servlet.adminPanel.movie_managing;

import com.manzar.controller.servlet.RequestParser;
import com.manzar.persistence.dao.MovieDao;
import com.manzar.persistence.dao.impl.MovieDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteMovie/*")
public class DeleteMovieServlet extends HttpServlet {

    private MovieDao movieDao;

    @Override
    public void init() throws ServletException {
        movieDao = MovieDaoImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestParser.parseIdFromRequest(req);
        movieDao.remove(id);
        resp.sendRedirect("/manageMovies");
    }
}
