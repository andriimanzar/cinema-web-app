package com.manzar.validator;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "passwordValidationFilter", urlPatterns = "/success")
public class PasswordValidator implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String password = servletRequest.getParameter("password");
        String repeatedPassword = servletRequest.getParameter("repeatedPassword");
        if (!validatePassword(password)) {
            // todo: type this to user
           httpServletResponse.sendRedirect("/register");
        }
        else if (!password.equals(repeatedPassword)) {
            // todo: type this to user
            httpServletResponse.sendRedirect("/register");
        }
       else filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean validatePassword(String password) {
        boolean containsUpperCaseLetter = password.chars().anyMatch(Character::isUpperCase);
        boolean containsNumericValue = password.chars().anyMatch(Character::isDigit);
        return containsUpperCaseLetter && containsNumericValue && password.length() >= 8;
    }
}
