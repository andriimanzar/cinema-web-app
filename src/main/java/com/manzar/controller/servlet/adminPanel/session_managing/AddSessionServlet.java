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
import java.time.LocalDateTime;

@WebServlet("/addSession")
public class AddSessionServlet extends HttpServlet {

    private MovieSessionDao movieSessionDao;

    @Override
    public void init() throws ServletException {
        movieSessionDao = MovieSessionDaoImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/add-session.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        saveSession(req);
        resp.sendRedirect("/manageSessions");
    }

    private void saveSession(HttpServletRequest request) {
        Long movieId = Long.valueOf(request.getParameter("movieId"));
        LocalDateTime showTime = LocalDateTime.parse(request.getParameter("showTime"));
        MovieSession movieSession = new MovieSession(movieId, showTime);
        movieSessionDao.save(movieSession);
    }
}
