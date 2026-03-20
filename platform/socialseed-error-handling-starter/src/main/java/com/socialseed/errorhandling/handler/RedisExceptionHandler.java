package com.socialseed.errorhandling.handler;

import com.socialseed.apiresponse.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RedisExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RedisExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleRedis(Exception ex) throws Exception {
        if (ex.getClass().getName().contains("Redis")) {
            log.error("Redis error: {}", ex.getMessage(), ex);
            String message = ApiResponse.msg("error.cache.redis");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(ApiResponse.error(HttpStatus.SERVICE_UNAVAILABLE.value(), message));
        }
        throw ex;
    }
}
