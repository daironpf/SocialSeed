---
ID: 006
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: platform, socialseed-error-handling-starter
---

# 006 - PGSQLExceptionHandler not annotated

## 1. Descripción Detallada
El `PGSQLExceptionHandler` en el starter `socialseed-error-handling-starter` no tenía la anotación `@RestControllerAdvice`. Sin esta anotación, Spring no escanea la clase como manejador de excepciones, por lo que las violaciones de integridad de PostgreSQL no se manejaban correctamente.

**Archivo afectado:** `platform/socialseed-error-handling-starter/src/main/java/com/socialseed/errorhandling/exceptionhandler/PGSQLExceptionHandler.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir `@RestControllerAdvice` directamente a la clase existente.
  - Crear un nuevo handler desde cero con la anotación correcta.
- **Selección:** Se añadió `@RestControllerAdvice` a la clase existente. Es el fix mínimo y correcto.

## 3. Restricciones de Arquitectura
- Permanece en `platform/socialseed-error-handling-starter`, donde deben vivir los handlers centralizados.
- Sigue el patrón de `@RestControllerAdvice` para manejo global de excepciones.
- No se modifica la lógica de manejo, solo la detección por Spring.

## 4. Plan de Implementación y Testeo
- [x] Añadir `@RestControllerAdvice` a PGSQLExceptionHandler
- [x] Verificar que Spring escanea el handler correctamente
- [x] Test de integración: verificar que violaciones de integridad de PostgreSQL se manejan con respuesta API estándar

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los handlers de excepciones en starters deben estar correctamente anotados para que Spring los detecte.
- Centralizar el manejo de excepciones de base de datos mantiene los servicios limpios y consistentes.
- Esta corrección asegura que errores de integridad (unique constraint, foreign key) se traduzcan en respuestas API claras.
