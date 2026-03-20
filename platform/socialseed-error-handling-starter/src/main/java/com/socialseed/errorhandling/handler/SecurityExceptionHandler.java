package com.socialseed.errorhandling.handler;

import com.socialseed.apiresponse.model.ApiResponse;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(SecurityExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleSecurity(Exception ex) throws Exception {
        String className = ex.getClass().getName();
        if (className.contains("AccessDenied") || className.contains("Authentication")
                || className.contains("Security") || className.contains("Jwt")
                || className.contains("Authorization")) {
            if (className.contains("AccessDenied")) {
                log.warn("Access denied: {}", ex.getMessage());
                String message = ApiResponse.msg("error.forbidden");
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ApiResponse.error(HttpStatus.FORBIDDEN.value(), message));
            }
            log.warn("Security error: {} - {}", ex.getClass().getSimpleName(), ex.getMessage());
            String message = ApiResponse.msg("error.unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), message));
        }
        throw ex;
    }
}
