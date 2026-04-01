---
ID: 034
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: socialuser-service
---

# 034 - Commented out REST endpoints uncommented

## 1. Descripción Detallada
El `UserController` en socialuser-service tenía respuestas 404 comentadas en las líneas 81-84, 102-104, 111-116. Esto causaba que se retornara 200 con null en lugar de 404 cuando un recurso no existía.

**Archivo afectado:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/entry/rest/controller/UserController.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Descomentar las respuestas 404.
  - Reimplementar con ResponseEntity.notFound().
- **Selección:** Se descomentaron las respuestas 404 apropiadas. Mantiene la intención original del código.

## 3. Restricciones de Arquitectura
- Se mantiene en `entry.rest.controller`.
- Se usa ApiResponse del api-response-starter.

## 4. Plan de Implementación y Testeo
- [x] Descomentar respuestas 404 en UserController
- [x] Verificar compilación
- [x] Test: verificar que retorna 404 cuando recurso no existe

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Código comentado en producción causa comportamientos incorrectos.
- Esta corrección restaura el manejo correcto de errores 404.
