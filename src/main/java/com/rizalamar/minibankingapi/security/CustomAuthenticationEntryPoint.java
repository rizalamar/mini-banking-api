package com.rizalamar.minibankingapi.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String jsonResponse = """
                {
                    "code": 401,
                    "status": "UNAUTHORIZED",
                    "data": null,
                    "message": "You do not have unauthorize to access",
                    "timestamp": "%s"
                }
                """.formatted(LocalDateTime.now());
        response.getWriter().write(jsonResponse);
    }
}
