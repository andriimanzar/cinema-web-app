package com.manzar.validator;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "lastNameValidationFilter", urlPatterns = "/success")
public class LastNameValidator implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String lastName = servletRequest.getParameter("lastName");
        if (validateParam(lastName)) {
            // todo: type this to user
           httpServletResponse.sendRedirect("/register");
        }

        else filterChain.doFilter(servletRequest, servletResponse);
    }


    private boolean validateParam(String lastName) {
        return lastName.isEmpty() || !lastName.chars().allMatch(Character::isLetter);
    }
}
