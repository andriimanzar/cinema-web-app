package com.manzar.validator;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(filterName = "passwordValidationFilter", urlPatterns = "/success")
public class PasswordValidator implements Filter {

    public static final String PASSWORD_IS_INVALID =
            "Password must contain at least 1 uppercase letter and 1 number";
    public static final String PASSWORDS_ARE_NOT_EQUAL = "Password and repeated password is not equal";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String password = servletRequest.getParameter("password");
        String repeatedPassword = servletRequest.getParameter("repeatedPassword");
        if (!validatePassword(password)) {
            ValidatorUtils.sendErrorMessageAndRedirect(servletRequest, servletResponse, PASSWORD_IS_INVALID);

        } else if (!password.equals(repeatedPassword)) {
            ValidatorUtils.sendErrorMessageAndRedirect(servletRequest, servletResponse, PASSWORDS_ARE_NOT_EQUAL);

        } else filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean validatePassword(String password) {
        boolean containsUpperCaseLetter = password.chars().anyMatch(Character::isUpperCase);
        boolean containsNumericValue = password.chars().anyMatch(Character::isDigit);
        return containsUpperCaseLetter && containsNumericValue && password.length() >= 8;
    }
}
