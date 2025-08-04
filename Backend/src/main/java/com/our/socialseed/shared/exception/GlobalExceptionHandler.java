package com.our.socialseed.shared.exception;

import jakarta.validation.ConstraintViolationException;
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

    /**
     * Maneja excepciones que ocurren cuando fallan las validaciones de argumentos anotados
     * con @Valid en controladores, típicamente con cuerpos JSON.
     *
     * @param ex excepción que contiene los errores de validación
     * @return ResponseEntity con lista de errores por campo y status 400 (Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Extrae los errores de cada campo con su respectivo mensaje
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        // Cuerpo de respuesta estructurado con los errores
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
        // Mapea cada violación a un objeto campo-mensaje
        List<Map<String, String>> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> Map.of(
                        "field", violation.getPropertyPath().toString(),
                        "message", violation.getMessage()
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
        // Podrías loggear 'ex' aquí si deseas más trazabilidad
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error");
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid UUID format");
    }
}