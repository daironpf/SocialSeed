package com.socialseed.errorhandling.handler;

import com.socialseed.apiresponse.model.ApiResponse;
import com.socialseed.errorhandling.exception.BusinessException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * Manejo global de excepciones para todos los microservicios.
 * Devuelve siempre ApiResponse con multi-idioma.
 */
@RestControllerAdvice
public class GlobalErrorHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);

        /* ===== 400 – Validation errors ===== */
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
                // Obtenemos todos los mensajes de validación y los traducimos
                String message = ex.getConstraintViolations()
                                .stream()
                                .map(violation -> {
                                        String template = violation.getMessage(); // Ej: "{email.invalid}"
                                        return ApiResponse.msg(template.replaceAll("[{}]", ""));
                                })
                                .collect(Collectors.joining("; "));

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), message));
        }

        /* ===== Business Exceptions ===== */
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {

                // Mensaje localizado a partir del errorCode del negocio
                String message = ApiResponse.msg(
                                ex.getErrorCode().getCode(),
                                ex.getParams());

                return ResponseEntity
                                .status(ex.getErrorCode().getHttpStatus())
                                .body(ApiResponse.error(ex.getErrorCode().getHttpStatus().value(), message));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {

                // Mensaje general de validación (i18n)
                String message = ApiResponse.msg("error.validation");

                // Detalle de errores por campo (i18n)
                var fieldErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> {
                                        String code = error.getDefaultMessage();
                                        if (code != null && code.startsWith("{") && code.endsWith("}")) {
                                                return ApiResponse.msg(code.replaceAll("[{}]", ""));
                                        }
                                        return error.getDefaultMessage();
                                })
                                .toList();

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), fieldErrors, message));
        }

        /**
         * Maneja cualquier parámetro mal formado (UUID, Integer, Long, Enum, Boolean,
         * etc.)
         */
        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

                String paramName = ex.getName();
                Object invalidValue = ex.getValue();
                Class<?> requiredType = ex.getRequiredType();

                // Message localized using ApiResponse.msg
                String message = ApiResponse.msg(
                                "error.invalid_parameter",
                                paramName, invalidValue,
                                requiredType != null ? requiredType.getSimpleName() : "unknown");

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), message));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
                log.error("Unexpected error occurred", ex);

                // Generic internal error message
                String message = ApiResponse.msg("error.internal");

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message));
        }
}