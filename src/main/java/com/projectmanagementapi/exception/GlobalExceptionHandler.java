package com.projectmanagementapi.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, WebRequest request) {

        log.warn("Validation failed: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setPath(request.getDescription(false));
        error.setMessage("Validation failed for one or more fields");

        // Collect field-specific error messages
        List<String> validationMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();

        error.setErrors(validationMessages);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // -------------------------------------------------------------------------
    // 404 — RESOURCE NOT FOUND
    // -------------------------------------------------------------------------
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, WebRequest request) {

        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request
        );
    }

    // -------------------------------------------------------------------------
    // 400 — JSON PARSE ERRORS / INVALID INPUT FORMAT
    // -------------------------------------------------------------------------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJson(
            HttpMessageNotReadableException ex, WebRequest request) {

        Throwable cause = ex.getCause();

        if (cause instanceof InvalidFormatException ife) {
            // Wrong value for an existing field (e.g., invalid enum)
            String field = ife.getPath().get(0).getFieldName();
            String value = String.valueOf(ife.getValue());

            log.warn("Invalid value '{}' for field '{}'", value, field);

            return buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "Invalid value '" + value + "' for field '" + field + "'",
                    request
            );
        }

        if (cause instanceof UnrecognizedPropertyException upe) {
            // Unknown field in JSON input
            String property = upe.getPropertyName();

            log.warn("Unrecognized field '{}' in request body", property);

            return buildErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    "JSON parse error: Unrecognized field '" + property + "'",
                    request
            );
        }

        // Generic malformed JSON
        log.warn("Malformed JSON request: {}", ex.getMessage());

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON request",
                request
        );
    }

    // -------------------------------------------------------------------------
    // 500 — UNEXPECTED SERVER ERRORS
    // -------------------------------------------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex, WebRequest request) {

        log.error("Unexpected server error", ex);

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request
        );
    }

    // -------------------------------------------------------------------------
    // Helper for building consistent error responses
    // -------------------------------------------------------------------------
    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            WebRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                message,
                request.getDescription(false)
        );

        return new ResponseEntity<>(error, status);
    }
}
