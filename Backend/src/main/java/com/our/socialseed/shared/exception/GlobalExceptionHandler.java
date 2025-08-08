package com.our.socialseed.shared.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler centraliza el manejo de excepciones en toda la aplicación.
 * Esta clase intercepta y responde a errores comunes como validaciones fallidas,
 * tipos de datos incorrectos y errores de ejecución.
 *
 * Anotada con @RestControllerAdvice para aplicar a todos los controladores REST.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Maneja excepciones que ocurren cuando fallan las validaciones de argumentos anotados
     * con @Valid en controladores, típicamente con cuerpos JSON.
     *
     * @param ex excepción que contiene los errores de validación
     * @return ResponseEntity con lista de errores por campo y status 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Locale locale = LocaleContextHolder.getLocale();

        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", messageSource.getMessage(error, locale)
                ))
                .collect(Collectors.toList());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("errors", errors);

        return ResponseEntity.badRequest().body(responseBody);
    }

    /**
     * Maneja validaciones fallidas que no están ligadas a un @RequestBody, como por ejemplo
     * parámetros en la URL o en métodos de servicio validados manualmente.
     *
     * @param ex excepción que contiene las violaciones de restricciones
     * @return ResponseEntity con detalles de los errores y status 400 (Bad Request)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolations(ConstraintViolationException ex) {
        Locale locale = LocaleContextHolder.getLocale();

        List<Map<String, String>> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> Map.of(
                        "field", violation.getPropertyPath().toString(),
                        "message", messageSource.getMessage(violation.getMessage(), null, locale)
                ))
                .collect(Collectors.toList());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("errors", errors);

        return ResponseEntity.badRequest().body(responseBody);
    }
    /**
     * Captura excepciones generales en tiempo de ejecución.
     * Este es un mecanismo de seguridad para evitar que errores no controlados expongan información sensible.
     *
     * @param ex excepción de tipo RuntimeException
     * @return ResponseEntity con mensaje genérico y status 500 (Internal Server Error)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        System.out.println(messageSource.getMessage("error.unexpected", null, new Locale("es")));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(messageSource.getMessage("error.unexpected", null, LocaleContextHolder.getLocale()));
    }

    /**
     * Maneja errores cuando un parámetro no puede convertirse al tipo esperado,
     * como al enviar un string en lugar de un UUID.
     *
     * @param ex excepción por tipo de argumento incorrecto
     * @return ResponseEntity con mensaje y status 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        System.out.println(messageSource.getMessage("error.invalid.uuid", null, new Locale("es")));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(messageSource.getMessage("error.invalid.uuid", null, LocaleContextHolder.getLocale()));
    }
}