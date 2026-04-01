---
ID: 018
Estado: hecha
Tipo: Bug
Prioridad: Baja
Impacto: socialuser-service
---

# 018 - Logger class name typo in DeleteUserValidator

## 1. Descripción Detallada
El `DeleteUserValidator` tenía un typo en la clase pasada al `LoggerFactory.getLogger()`: usaba `CreateUserValidator.class` en lugar de `DeleteUserValidator.class`. Esto causaba que los logs mostraran el nombre de clase incorrecto.

**Archivo afectado:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/application/usecase/validation/DeleteUserValidator.java` (línea 17)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Corregir la referencia de clase.
  - Usar `MethodHandles.lookup().lookupClass()` (Java 7+).
- **Selección:** Se corrigió la referencia a `DeleteUserValidator.class`. Es el fix mínimo y correcto.

## 3. Restricciones de Arquitectura
- Se mantiene en `application.usecase.validation`.
- No se modifica la lógica de validación.

## 4. Plan de Implementación y Testeo
- [x] Corregir LoggerFactory.getLogger(DeleteUserValidator.class)
- [x] Verificar compilación
- [x] Verificar que logs muestran nombre correcto

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los logs con nombre de clase incorrecto dificultan el debugging.
- Este tipo de bugs son comunes al copiar-pegar código.
