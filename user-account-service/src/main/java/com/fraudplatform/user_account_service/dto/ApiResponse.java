package com.fraudplatform.user_account_service.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Getter
@Builder
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private String error;
    private Instant timestamp;

    public static <T> ApiResponse<T> success(T data, String message, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String error, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status(status.value())
                .error(error)
                .timestamp(Instant.now())
                .build();
    }
}
