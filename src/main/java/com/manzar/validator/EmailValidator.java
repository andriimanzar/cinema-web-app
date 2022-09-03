package com.manzar.validator;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "emailValidationFilter", urlPatterns = "/success")
public class EmailValidator implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String email = servletRequest.getParameter("email");
        if (!emailIsValid(email)) {
            // todo: type this to user
            httpServletResponse.sendRedirect("/register");
        } else filterChain.doFilter(servletRequest, servletResponse);
    }


    private boolean emailIsValid(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
