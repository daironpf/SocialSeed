package com.socialseed.authservice.platform.error;

import com.socialseed.authservice.platform.common.response.ApiResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusiness(BusinessException ex) {
        String message = messageSource.getMessage(
                ex.getErrorCode().getCode(),
                ex.getParams(),
                LocaleContextHolder.getLocale()
        );

        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(ApiResponse.error(ex.getErrorCode().getHttpStatus().value(), message));
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

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {
        String message = messageSource.getMessage("error.internal", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(500)
                .body(ApiResponse.error(500, message));
    }
}
