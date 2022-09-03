package com.manzar.validator;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "firstNameValidationFilter", urlPatterns = "/success")
public class FirstNameValidator implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String firstName = servletRequest.getParameter("firstName");
        if (validateLastName(firstName)) {
            // todo: type this to user
            httpServletResponse.sendRedirect("/register");
        } else filterChain.doFilter(servletRequest, servletResponse);
    }


    private boolean validateLastName(String firstName) {
        return firstName.isEmpty() || !firstName.chars().allMatch(Character::isLetter);
    }
}
