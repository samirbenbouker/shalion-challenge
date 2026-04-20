package com.shalion.challenge.exception;

import java.time.Instant;

import com.shalion.challenge.util.AppMessages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles bean validation errors for request bodies.
     *
     * @param ex validation exception
     * @param request HTTP request
     * @return standardized bad-request payload
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse(AppMessages.ERROR_INVALID_REQUEST_MESSAGE);

        return buildError(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    /**
     * Handles constraint violations from request parameters/path variables.
     *
     * @param ex validation exception
     * @param request HTTP request
     * @return standardized bad-request payload
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintValidation(ConstraintViolationException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handles malformed JSON payloads.
     *
     * @param ex parsing exception
     * @param request HTTP request
     * @return standardized bad-request payload
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleMalformedJson(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, AppMessages.ERROR_MALFORMED_REQUEST_BODY_MESSAGE, request.getRequestURI());
    }

    /**
     * Handles resource-not-found errors.
     *
     * @param ex not-found exception
     * @param request HTTP request
     * @return standardized not-found payload
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handles business-conflict errors.
     *
     * @param ex conflict exception
     * @param request HTTP request
     * @return standardized conflict payload
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Handles persistence-level integrity violations.
     *
     * @param ex data integrity exception
     * @param request HTTP request
     * @return standardized conflict payload
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        return buildError(HttpStatus.CONFLICT, AppMessages.ERROR_DATA_INTEGRITY_VIOLATION_MESSAGE, request.getRequestURI());
    }

    /**
     * Handles illegal argument usage in request processing.
     *
     * @param ex illegal argument exception
     * @param request HTTP request
     * @return standardized bad-request payload
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    /**
     * Fallback handler for unexpected errors.
     *
     * @param ex generic exception
     * @param request HTTP request
     * @return standardized internal-server-error payload
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, AppMessages.ERROR_UNEXPECTED_ERROR_MESSAGE, request.getRequestURI());
    }

    /**
     * Creates the common API error response payload.
     *
     * @param status HTTP status to return
     * @param message human-readable error message
     * @param path request path
     * @return response entity with {@link ApiError}
     */
    private ResponseEntity<ApiError> buildError(HttpStatus status, String message, String path) {
        ApiError apiError = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );

        return ResponseEntity.status(status).body(apiError);
    }
}
