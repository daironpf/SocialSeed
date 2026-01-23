package com.socialseed.errorhandling.handler;

import com.socialseed.apiresponse.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Especialista en capturar y procesar errores de persistencia en PostgreSQL.
 */
@ConditionalOnClass(DataIntegrityViolationException.class)
public class PGSQLExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(PGSQLExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.error("PostgreSQL Data Integrity Violation", ex);

        String rootMsg = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
        String displayMsg = ApiResponse.msg("error.database.integrity");

        // Si es un error de duplicado (Unique constraint)
        if (rootMsg != null && rootMsg.contains("duplicate key value")) {
            displayMsg = ApiResponse.msg("error.resource_already_exists", "");
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(HttpStatus.CONFLICT.value(), displayMsg));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), displayMsg));
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse<Void>> handleSQLException(SQLException ex) {
        log.error("Raw SQL Exception", ex);
        String message = ApiResponse.msg("error.database.raw", ex.getSQLState());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message));
    }
}
