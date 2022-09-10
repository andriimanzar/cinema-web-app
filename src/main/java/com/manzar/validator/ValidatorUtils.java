package com.manzar.validator;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ValidatorUtils {

    public static void sendErrorMessageAndRedirect(ServletRequest servletRequest, ServletResponse servletResponse,
                                                   String errorMessage) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("errorMessage", errorMessage);
        httpServletResponse.sendRedirect("/register");
    }
}
