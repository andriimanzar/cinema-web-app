package com.manzar.validator;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(filterName = "phoneNumberValidationFilter", urlPatterns = "/success")
public class PhoneNumberValidator implements Filter {

    public static final String PHONE_NUMBER_IS_INVALID = "Phone number is invalid!";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        if (!phoneNumberIsValid(servletRequest.getParameter("phoneNumber"))) {
            ValidatorUtils.sendErrorMessageAndRedirect(servletRequest, servletResponse, PHONE_NUMBER_IS_INVALID);

        } else filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean phoneNumberIsValid(String phoneNumber) {
        return phoneNumber.matches("^\\+(?:[0-9] ?){6,14}[0-9]$");
    }
}

