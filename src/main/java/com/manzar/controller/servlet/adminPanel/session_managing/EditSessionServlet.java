package com.manzar.controller.servlet.adminPanel.session_managing;

import com.manzar.controller.servlet.RequestParser;
import com.manzar.persistence.dao.MovieSessionDao;
import com.manzar.persistence.dao.impl.MovieSessionDaoImpl;
import com.manzar.persistence.entity.MovieSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/editSession/*")
public class EditSessionServlet extends HttpServlet {

    private MovieSessionDao movieSessionDao;

    @Override
    public void init() throws ServletException {
        movieSessionDao = MovieSessionDaoImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestParser.parseIdFromRequest(req);
        MovieSession movieSessionFromDb = movieSessionDao.findById(id);
        SessionRequestHelper.sendMovieSessionThroughRequest(req, movieSessionFromDb);
        req.getRequestDispatcher("/WEB-INF/edit-session.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = RequestParser.parseIdFromRequest(req);
        parseMovieFromRequestAndUpdate(req, id);
        resp.sendRedirect("/manageSessions");
    }

    private void parseMovieFromRequestAndUpdate(HttpServletRequest request, Long id) {
        MovieSession movieSession = SessionRequestHelper.parseMovieSessionFromRequest(request);
        movieSession.setId(id);
        movieSessionDao.update(movieSession);
    }
}
