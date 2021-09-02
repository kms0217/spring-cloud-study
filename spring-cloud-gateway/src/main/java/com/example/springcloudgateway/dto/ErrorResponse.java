package com.example.springcloudgateway.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private String message;
    private LocalDateTime localDateTime;
    private Map<String, Object> infos = new HashMap<>();

    public static ErrorResponse defaultError(String message) {
        return new ErrorResponse(message, LocalDateTime.now());
    }

    public ErrorResponse(String message, LocalDateTime localDateTime) {
        this.message = message;
        this.localDateTime = localDateTime;
    }
}
