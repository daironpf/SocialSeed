package com.our.socialseed.shared.exception;

import com.our.socialseed.auth.config.exception.EmailAlreadyExistsException;
import com.our.socialseed.shared.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler centraliza el manejo de excepciones en toda la aplicación.
 * Ahora utiliza ApiResponse e internacionalización.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    // --------------------------------------------
    // Validaciones de @Valid en @RequestBody
    // --------------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Locale locale = LocaleContextHolder.getLocale();

        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", messageSource.getMessage(error, locale)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(),
                        messageSource.getMessage("error.validation", null, locale) + ": " + errors));
    }

    // --------------------------------------------
    // ConstraintViolationException (validaciones fuera de @RequestBody)
    // --------------------------------------------
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<?>> handleConstraintViolations(ConstraintViolationException ex) {
        Locale locale = LocaleContextHolder.getLocale();

        List<Map<String, String>> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> Map.of(
                        "field", violation.getPropertyPath().toString(),
                        "message", messageSource.getMessage(violation.getMessage(), null, locale)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(),
                        messageSource.getMessage("error.validation", null, locale) + ": " + errors));
    }

    // --------------------------------------------
    // Excepción específica: Email ya registrado
    // --------------------------------------------
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<?>> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage("auth.register.email.exists", null, locale);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.conflict(message));
    }

    // --------------------------------------------
    // Error de tipo de argumento (ej: UUID inválido)
    // --------------------------------------------
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage("error.invalid.uuid", null, locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(HttpStatus.BAD_REQUEST.value(), message));
    }

    // --------------------------------------------
    // Excepciones generales en tiempo de ejecución
    // --------------------------------------------
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage("error.unexpected", null, locale);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message));
    }
}
