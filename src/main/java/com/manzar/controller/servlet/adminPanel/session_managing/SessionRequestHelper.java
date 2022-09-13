package com.manzar.controller.servlet.adminPanel.session_managing;

import com.manzar.persistence.entity.MovieSession;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

public class SessionRequestHelper {

    private SessionRequestHelper() {

    }

    public static MovieSession parseMovieSessionFromRequest(HttpServletRequest request) {
        Long movieId = Long.valueOf(request.getParameter("movieId"));
        LocalDateTime showTime = LocalDateTime.parse(request.getParameter("showTime"));
        return new MovieSession(movieId, showTime);
    }

    public static void sendMovieSessionThroughRequest(HttpServletRequest request, MovieSession movieSession) {
        request.setAttribute("movieIdFromDb", movieSession.getMovieId());
        request.setAttribute("showTimeFromDb", movieSession.getShowTime());
        request.setAttribute("sessionId", movieSession.getId());
    }
}
