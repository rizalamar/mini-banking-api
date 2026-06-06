package com.rizalamar.minibankingapi.controller;

import com.rizalamar.minibankingapi.dto.WebResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<String>> constraintViolationException(ConstraintViolationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.<String>builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .status("BAD REQUEST")
                        .data(null)
                        .message(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<String>> responseStatusException(ResponseStatusException exception){
        return ResponseEntity.status(exception.getStatusCode())
                .body(WebResponse.<String>builder()
                        .code(exception.getStatusCode().value())
                        .status(exception.getStatusCode().toString())
                        .data(null)
                        .message(exception.getReason())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<String>> internalServerError(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.<String>builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status("INTERNAL SERVER ERROR")
                        .data(null)
                        .message("An unexpected error occured: " + exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

}
