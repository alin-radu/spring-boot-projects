package com.dev.ecom_platform_2.exception;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    // Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex, HttpServletRequest request) {
        log.error("Caught Exception.", ex);

        var httpStatus = HttpStatus.BAD_REQUEST.value();
        var errorResponse = ApiResponse.builder()
                .status(httpStatus)
                .message("An unexpected error occurred.")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // IllegalArgumentException
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.error("Caught IllegalArgumentException.", ex);

        var httpStatus = HttpStatus.BAD_REQUEST.value();
        var errorResponse = ApiResponse.builder()
                .status(httpStatus)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // MethodArgumentTypeMismatchException
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.error("Caught MethodArgumentTypeMismatchException.", ex);

        var httpStatus = HttpStatus.BAD_REQUEST.value();
        var errorResponse = ApiResponse.builder()
                .status(httpStatus)
                .message("Invalid request arguments!")
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // IllegalStateException
    @ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<ApiResponse> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        log.error("Caught IllegalStateException.", ex);

        var httpStatus = HttpStatus.CONFLICT.value();
        var errorResponse = ApiResponse.builder()
                .status(httpStatus)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // APIException
    @ExceptionHandler(APIException.class)
    public ResponseEntity<ApiResponse> handleAPIException(APIException ex, WebRequest request) {
        log.error("Caught APIException.", ex);

        var httpStatus = HttpStatus.BAD_REQUEST.value();
        var errorResponse = ApiResponse.builder()
                .status(httpStatus)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // EntityNotFoundException
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.error("Caught EntityNotFoundException.", ex);

        var httpStatus = HttpStatus.NOT_FOUND.value();
        var errorResponse = ApiResponse.builder()
                .status(httpStatus)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ApiResponse> handleItemNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error("Caught ResourceNotFoundException.", ex);

        var httpStatus = HttpStatus.NOT_FOUND.value();
        var errorResponse = ApiResponse.builder()
                .status(httpStatus)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // UsernameNotFoundException
    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<ApiResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        log.error("Caught UsernameNotFoundException.", ex);

        var httpStatus = HttpStatus.UNAUTHORIZED.value();
        var errorResponse = ApiResponse.builder()
                .status(httpStatus)
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getDescription(false))
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    // MethodArgumentNotValidException
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @Nullable MethodArgumentNotValidException ex, @Nullable HttpHeaders headers, @Nullable HttpStatusCode status, @Nullable WebRequest request) {
        log.error("Caught MethodArgumentNotValidException.", ex);

        String message = "Validation failed!";
        List<ApiResponse.FieldError> errors = null;
        if (ex != null) {
            message = null;
            errors = ex.getBindingResult().getFieldErrors().stream()
                    .map(fieldError ->
                            new ApiResponse.FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                    .toList();
        }

        String path = request != null ? request.getDescription(false) : null;

        var httpStatus = HttpStatus.BAD_REQUEST.value();
        var errorResponse = ApiResponse.builder()
                .status(httpStatus)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
