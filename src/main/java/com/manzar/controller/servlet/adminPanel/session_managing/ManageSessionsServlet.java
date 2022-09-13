package com.manzar.controller.servlet.adminPanel.session_managing;

import com.manzar.persistence.dao.MovieSessionDao;
import com.manzar.persistence.dao.impl.MovieSessionDaoImpl;
import com.manzar.persistence.entity.MovieSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

@WebServlet("/manageSessions")
public class ManageSessionsServlet extends HttpServlet {

    private MovieSessionDao movieSessionDao;

    @Override
    public void init() {
        movieSessionDao = MovieSessionDaoImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<MovieSession, String> map = movieSessionDao.findAllMovieSessions();
        req.setAttribute("movieSessionMap", map);
        req.getRequestDispatcher("/WEB-INF/manage-sessions.jsp").forward(req, resp);
    }
}
