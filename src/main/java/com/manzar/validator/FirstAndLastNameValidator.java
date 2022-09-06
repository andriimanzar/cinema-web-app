package com.manzar.validator;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(filterName = "firstAndLastNameValidationFilter", urlPatterns = "/success")
public class FirstAndLastNameValidator implements Filter {

    public static final String FIRST_NAME_IS_INVALID = "First name must be not empty and contain only letters!";
    public static final String LAST_NAME_IS_INVALID = "Last name must be not empty and contain only letters!";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        String firstName = servletRequest.getParameter("firstName");
        String lastName = servletRequest.getParameter("lastName");
        if (validateString(firstName)) {
            ValidatorUtils.sendErrorMessageAndRedirect(servletRequest, servletResponse, FIRST_NAME_IS_INVALID);

        } else if (validateString(lastName)) {
            ValidatorUtils.sendErrorMessageAndRedirect(servletRequest, servletResponse, LAST_NAME_IS_INVALID);

        } else filterChain.doFilter(servletRequest, servletResponse);
    }


    public static boolean validateString(String input) {
        return input.isEmpty() || !input.chars().allMatch(Character::isLetter);
    }
}
