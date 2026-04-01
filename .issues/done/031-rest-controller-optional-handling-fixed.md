---
ID: 031
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: socialuser-service
---

# 031 - REST Controller Optional handling fixed

## 1. Descripción Detallada
El `UserController` en socialuser-service llamaba `.get()` sobre un `Optional` sin verificar `isPresent()` en la línea 58. Esto lanzaba `NoSuchElementException` cuando el usuario no existía.

**Archivo afectado:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/entry/rest/controller/UserController.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Usar `Optional.map().orElseThrow()`.
  - Verificar `isPresent()` manualmente.
- **Selección:** Se usó `Optional.map().orElseThrow()` con BusinessException. Es el enfoque más limpio y moderno de Java.

## 3. Restricciones de Arquitectura
- Se mantiene en `entry.rest.controller`.
- Se usa BusinessException del error-handling starter.

## 4. Plan de Implementación y Testeo
- [x] Corregir manejo de Optional en UserController
- [x] Verificar compilación
- [x] Test: verificar que retorna 404 cuando usuario no existe

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Llamar .get() sin verificar es un bug común en Java.
- Esta corrección previene errores 500 innecesarios.
