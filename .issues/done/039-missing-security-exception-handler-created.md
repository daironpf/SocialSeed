---
ID: 039
Estado: hecha
Tipo: Feature
Prioridad: Media
Impacto: platform, socialseed-error-handling-starter
---

# 039 - Missing SecurityExceptionHandler created

## 1. Descripción Detallada
El starter `socialseed-error-handling-starter` no tenía un manejador de excepciones de seguridad (`AccessDeniedException`, `AuthenticationException`, `JwtException`). Los errores de seguridad no se traducían en respuestas API estándar.

**Archivo creado:** `platform/socialseed-error-handling-starter/src/main/java/com/socialseed/errorhandling/exceptionhandler/SecurityExceptionHandler.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Crear handler especializado para seguridad.
  - Manejar errores de seguridad en el GlobalErrorHandler.
- **Selección:** Se creó `SecurityExceptionHandler` con `@RestControllerAdvice`. Mantiene el GlobalErrorHandler limpio y proporciona manejo específico para errores de seguridad.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-error-handling-starter`.
- Usa `@RestControllerAdvice` para tipos específicos de excepción de seguridad.

## 4. Plan de Implementación y Testeo
- [x] Crear SecurityExceptionHandler con @RestControllerAdvice
- [x] Manejar AccessDeniedException, AuthenticationException, JwtException
- [x] Verificar que Spring escanea el handler
- [x] Test de integración: verificar que errores de seguridad se manejan correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los handlers de seguridad especializados proporcionan respuestas claras para errores de autenticación/autorización.
- Esta corrección mejora la experiencia del usuario cuando ocurren errores de seguridad.
