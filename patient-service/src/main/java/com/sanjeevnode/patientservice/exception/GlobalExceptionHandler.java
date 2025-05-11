package com.sanjeevnode.patientservice.exception;

import com.sanjeevnode.patientservice.utils.ApiResponse;
import com.sanjeevnode.patientservice.utils.AppLogger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final AppLogger log = new AppLogger(GlobalExceptionHandler.class, "GlobalExceptionHandler");

    private ResponseEntity<ApiResponse> buildErrorResponse(HttpStatus status, String message, Object data) {
        return ResponseEntity.status(status).body(
                ApiResponse.builder()
                        .status(status)
                        .message(message)
                        .data(data)
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage())
        );
        log.warn("Validation failed: %s", errors.toString());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation error", errors);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        log.warn(ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, "Email address already exists", null);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ApiResponse> handlePatientNotFoundException(PatientNotFoundException ex) {
        log.warn(ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Patient not found", null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error: %s", ex, ex.getMessage());
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", null);
    }
}
