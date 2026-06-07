package com.rizalamar.minibankingapi.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        String jsonResponse = """
                {
                    "code": 403,
                    "status": "FORBIDDEN",
                    "data": null,
                    "message": "You do not have permission to access",
                    "timestamp": "%s"
                }
                """.formatted(LocalDateTime.now());
        response.getWriter().write(jsonResponse);
    }
}
