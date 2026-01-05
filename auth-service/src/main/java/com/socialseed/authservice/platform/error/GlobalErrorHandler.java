package com.socialseed.authservice.platform.error;

import com.socialseed.authservice.platform.common.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Manejo global de excepciones para todos los microservicios.
 * Devuelve siempre ApiResponse con multi-idioma.
 */
@RestControllerAdvice
public class GlobalErrorHandler {

    private final MessageSource messageSource;

    public GlobalErrorHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /* ===== 409 – Business conflict ===== */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusiness(BusinessException ex) {

        // Mensaje localizado a partir del errorCode del negocio
        String message = messageSource.getMessage(
                ex.getErrorCode().getCode(),
                ex.getParams(),
                LocaleContextHolder.getLocale()
        );

        // Construcción del ApiResponse usando la nueva factory
        ApiResponse<?> response = ApiResponse.message(
                ex.getErrorCode().getHttpStatus().value(),
                message
        );

        return ResponseEntity
                .status(ex.getErrorCode().getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {

        // Mensaje general de validación (i18n)
        String message = messageSource.getMessage(
                "error.validation",
                null,
                LocaleContextHolder.getLocale()
        );

        // Detalle de errores por campo (i18n)
        var fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                .toList();

        // Construcción del ApiResponse usando la nueva factory
        ApiResponse<?> response = ApiResponse.of(
                HttpStatus.BAD_REQUEST.value(),
                fieldErrors,   // data
                message        // mensaje general
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    /**
     * Maneja cualquier parámetro mal formado (UUID, Integer, Long, Enum, Boolean, etc.)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        String paramName = ex.getName();
        Object invalidValue = ex.getValue();
        Class<?> requiredType = ex.getRequiredType();

        // Mensaje genérico configurable en messages.properties
        String message = messageSource.getMessage(
                "error.invalid_parameter",
                new Object[]{paramName, invalidValue, requiredType != null ? requiredType.getSimpleName() : "unknown"},
                LocaleContextHolder.getLocale()
        );

        // Construcción del ApiResponse usando la nueva factory
        ApiResponse<?> response = ApiResponse.message(
                HttpStatus.BAD_REQUEST.value(),
                message
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {

        // Logueo del error para depuración y trazabilidad
        // (muy recomendable en producción)
        final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);
        log.error("Unexpected error occurred", ex);


        // Mensaje genérico configurable en messages.properties
        String message = messageSource.getMessage(
                "error.internal",
                null,
                LocaleContextHolder.getLocale()
        );

        // Construcción del ApiResponse usando la nueva factory
        ApiResponse<?> response = ApiResponse.message(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                message
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}