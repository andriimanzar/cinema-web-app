package com.manzar.controller.servlet;

import jakarta.servlet.http.HttpServletRequest;

public class RequestParser {

    public static Long parseIdFromRequest(HttpServletRequest httpServletRequest) {
        return Long.valueOf(httpServletRequest.getPathInfo().split("/")[1]);
    }
}
