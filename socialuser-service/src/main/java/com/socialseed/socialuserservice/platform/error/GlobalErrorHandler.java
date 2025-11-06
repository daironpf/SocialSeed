package com.socialseed.socialuserservice.platform.error;

import com.socialseed.socialuserservice.platform.common.response.ApiResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGeneric(Exception ex) {
        String message = messageSource.getMessage("error.internal", null, LocaleContextHolder.getLocale());
        return ResponseEntity.status(500)
                .body(ApiResponse.error(500, message));
    }
}
