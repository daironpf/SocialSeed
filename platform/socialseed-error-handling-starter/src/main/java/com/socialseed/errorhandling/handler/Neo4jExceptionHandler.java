package com.socialseed.errorhandling.handler;

import com.socialseed.apiresponse.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Neo4jExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(Neo4jExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("neo4j")) {
            log.error("Neo4j DataIntegrityViolationException: {}", ex.getMessage(), ex);
            String message = ApiResponse.msg("error.database.integrity");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), message));
        }
        throw ex;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) throws Exception {
        if (ex.getClass().getName().contains("Neo4j")) {
            log.error("Neo4j error: {}", ex.getMessage(), ex);
            String message = ApiResponse.msg("error.database.neo4j");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message));
        }
        if (ex.getMessage() != null && ex.getMessage().contains("neo4j")) {
            log.error("Neo4j error: {}", ex.getMessage(), ex);
            String message = ApiResponse.msg("error.database.neo4j");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message));
        }
        throw ex;
    }
}
