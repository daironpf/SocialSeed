package com.socialseed.errorhandling.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.errorhandling.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @ExceptionHandler(BusinessException.class)
    public void handleBusiness(BusinessException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("GlobalErrorHandler.handleBusiness() for {} on {}", ex.getErrorCode(), request.getRequestURI());
        int status = ex.getErrorCode().getHttpStatus().value();
        String resolvedMsg;
        try {
            resolvedMsg = ApiResponse.msg(ex.getErrorCode().getCode(), ex.getParams());
        } catch (Exception e) {
            log.warn("Failed to resolve message for '{}': {}", ex.getErrorCode().getCode(), e.getMessage());
            resolvedMsg = ex.getErrorCode().getCode();
        }
        writeError(response, status, resolvedMsg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("GlobalErrorHandler.handleConstraintViolation() for {}", request.getRequestURI());
        String message = ex.getConstraintViolations().stream()
                .map(v -> ApiResponse.msg(v.getMessage().replaceAll("[{}]", "")))
                .collect(Collectors.joining("; "));
        writeError(response, HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
        writeError(response, HttpStatus.BAD_REQUEST.value(), String.join("; ", fieldErrors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("GlobalErrorHandler.handleTypeMismatch() for {}", request.getRequestURI());
        String message = ApiResponse.msg("error.invalid_parameter",
                ex.getName(), ex.getValue(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");
        writeError(response, HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(Exception.class)
    public void handleGeneric(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("GlobalErrorHandler.handleGeneric() for {}: {} - {}", request.getRequestURI(), ex.getClass().getName(), ex.getMessage(), ex);
        writeError(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), ApiResponse.msg("error.internal"));
    }

    private void writeError(HttpServletResponse response, int status, String message) throws IOException {
        if (!response.isCommitted()) {
            response.resetBuffer();
            response.setStatus(status);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            var body = new ApiResponse<Void>(status, null, message, "v0.0.1", Instant.now());
            response.getWriter().write(mapper.writeValueAsString(body));
            response.getWriter().flush();
        }
    }
}
