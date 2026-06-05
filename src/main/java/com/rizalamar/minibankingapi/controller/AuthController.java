package com.rizalamar.minibankingapi.controller;

import com.rizalamar.minibankingapi.dto.auth.AuthResponse;
import com.rizalamar.minibankingapi.dto.auth.LoginRequest;
import com.rizalamar.minibankingapi.dto.auth.RegisterRequest;
import com.rizalamar.minibankingapi.dto.auth.WebResponse;
import com.rizalamar.minibankingapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<AuthResponse>> register(@RequestBody RegisterRequest request){
        AuthResponse register = authService.register(request);

        WebResponse<AuthResponse> response = WebResponse.<AuthResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data(register)
                .message("User registered successfully")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WebResponse<AuthResponse> >login(@RequestBody LoginRequest request){
        AuthResponse login = authService.login(request);
        WebResponse<AuthResponse> response = WebResponse.<AuthResponse>builder()
                .code(HttpStatus.OK.value())
                .status("OK")
                .data(login)
                .message("User login successfully")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
