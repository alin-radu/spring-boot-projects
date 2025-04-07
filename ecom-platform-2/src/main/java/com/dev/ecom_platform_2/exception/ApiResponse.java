package com.dev.ecom_platform_2.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiResponse {
    private int status;
    private String message;
    private List<FieldError> errors;

    private LocalDateTime timestamp;  // timestamp of when the error occurred
    private String path;             // the request path that caused the error

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }
}