package com.socialseed.errorhandling.handler;

import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.errorhandling.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.socialseed")
public class GlobalErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex, HttpServletRequest request) {
        log.info("GlobalErrorHandler.handleBusiness() for {} on {}", ex.getErrorCode(), request.getRequestURI());
        int status = ex.getErrorCode().getHttpStatus().value();
        String resolvedMsg;
        try {
            resolvedMsg = ApiResponse.msg(ex.getErrorCode().getCode(), ex.getParams());
        } catch (Exception e) {
            log.warn("Failed to resolve message for '{}': {}", ex.getErrorCode().getCode(), e.getMessage());
            resolvedMsg = ex.getErrorCode().getCode();
        }
        return ResponseEntity.status(status).body(new ApiResponse<>(status, null, resolvedMsg, "v0.0.1", Instant.now()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        log.info("GlobalErrorHandler.handleConstraintViolation() for {}", request.getRequestURI());
        String message = ex.getConstraintViolations().stream()
                .map(v -> ApiResponse.msg(v.getMessage().replaceAll("[{}]", "")))
                .collect(Collectors.joining("; "));
        int status = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(status).body(new ApiResponse<>(status, null, message, "v0.0.1", Instant.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.info("GlobalErrorHandler.handleValidationErrors() for {}", request.getRequestURI());
        String message = ApiResponse.msg("error.validation");
        var fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> {
                    String code = error.getDefaultMessage();
                    return (code != null && code.startsWith("{") && code.endsWith("}"))
                        ? ApiResponse.msg(code.replaceAll("[{}]", ""))
                        : error.getDefaultMessage();
                })
                .toList();
        int status = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(status).body(new ApiResponse<>(status, null, String.join("; ", fieldErrors), "v0.0.1", Instant.now()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.info("GlobalErrorHandler.handleTypeMismatch() for {}", request.getRequestURI());
        String message = ApiResponse.msg("error.invalid_parameter",
                ex.getName(), ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        int status = HttpStatus.BAD_REQUEST.value();
        return ResponseEntity.status(status).body(new ApiResponse<>(status, null, message, "v0.0.1", Instant.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("GlobalErrorHandler.handleGeneric() for {}: {} - {}", request.getRequestURI(), ex.getClass().getName(), ex.getMessage(), ex);
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        return ResponseEntity.status(status).body(new ApiResponse<>(status, null, ApiResponse.msg("error.internal"), "v0.0.1", Instant.now()));
    }
}
