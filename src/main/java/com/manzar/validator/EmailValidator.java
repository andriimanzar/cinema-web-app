package com.manzar.validator;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(filterName = "emailValidationFilter", urlPatterns = "/success")
public class EmailValidator implements Filter {

    public static final String EMAIL_ADDRESS_IS_INVALID = "Email is invalid";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String email = servletRequest.getParameter("email");
        if (!emailIsValid(email)) {
            ValidatorUtils.sendErrorMessageAndRedirect(servletRequest, servletResponse, EMAIL_ADDRESS_IS_INVALID);
        } else filterChain.doFilter(servletRequest, servletResponse);
    }


    private boolean emailIsValid(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
