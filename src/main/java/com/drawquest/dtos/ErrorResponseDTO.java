package com.drawquest.dtos;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErrorResponseDTO {
    private final String errorCode;
    private final String message;
    private Map<String, String> details;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponseDTO(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorResponseDTO(String errorCode, String message, Map<String, String> details) {
        this.errorCode = errorCode;
        this.message = message;
        this.details = details;
    }

}
