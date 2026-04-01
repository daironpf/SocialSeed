---
ID: 021
Estado: hecha
Tipo: Feature
Prioridad: Media
Impacto: platform, socialseed-error-handling-starter
---

# 021 - Missing Neo4j and Redis exception handlers

## 1. Descripción Detallada
El starter `socialseed-error-handling-starter` no tenía manejadores de excepciones para Neo4j y Redis. Los errores de estas bases de datos no se traducían en respuestas API estándar.

**Archivos creados:**
- `platform/socialseed-error-handling-starter/src/main/java/com/socialseed/errorhandling/exceptionhandler/Neo4jExceptionHandler.java`
- `platform/socialseed-error-handling-starter/src/main/java/com/socialseed/errorhandling/exceptionhandler/RedisExceptionHandler.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Crear handlers especializados por tipo de base de datos.
  - Manejar todo en el GlobalErrorHandler genérico.
- **Selección:** Se crearon handlers especializados `Neo4jExceptionHandler` y `RedisExceptionHandler`. Mantiene el GlobalErrorHandler limpio y proporciona manejo específico por tecnología.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-error-handling-starter`.
- Cada handler usa `@RestControllerAdvice` para tipos específicos de excepción.
- Se usan mensajes i18n del api-response-starter.

## 4. Plan de Implementación y Testeo
- [x] Crear Neo4jExceptionHandler con @RestControllerAdvice
- [x] Crear RedisExceptionHandler con @RestControllerAdvice
- [x] Verificar que Spring escanea los handlers
- [x] Test de integración: verificar que errores de Neo4j/Redis se manejan correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Handlers especializados mantienen el código limpio y proporcionan mensajes de error específicos.
- Centralizar manejo de excepciones en el starter evita duplicación en servicios.
- Esta corrección mejora la experiencia del desarrollador y del usuario final.
