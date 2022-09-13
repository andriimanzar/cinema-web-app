package com.manzar.controller.servlet.adminPanel.session_managing;

import com.manzar.controller.servlet.RequestParser;
import com.manzar.persistence.dao.MovieSessionDao;
import com.manzar.persistence.dao.impl.MovieSessionDaoImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deleteSession/*")
public class DeleteSessionServlet extends HttpServlet {

    private MovieSessionDao movieSessionDao;

    @Override
    public void init() {
        movieSessionDao = MovieSessionDaoImpl.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = RequestParser.parseIdFromRequest(req);
        movieSessionDao.remove(id);
        resp.sendRedirect("/manageSessions");
    }
}


