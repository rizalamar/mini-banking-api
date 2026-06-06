package com.rizalamar.minibankingapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponse<T> {
    private int code;
    private String status;
    private T data;
    private String message;
    private LocalDateTime timestamp;
}
