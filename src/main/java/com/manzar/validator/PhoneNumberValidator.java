package com.manzar.validator;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter(filterName = "phoneNumberValidationFilter", urlPatterns = "/success")
public class PhoneNumberValidator implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if (!phoneNumberIsValid(servletRequest.getParameter("phoneNumber"))) {
            // todo: type this to user
            httpServletResponse.sendRedirect("/register");
        } else filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean phoneNumberIsValid(String phoneNumber) {
        return phoneNumber.matches("^\\+(?:[0-9] ?){6,14}[0-9]$");
    }
}

