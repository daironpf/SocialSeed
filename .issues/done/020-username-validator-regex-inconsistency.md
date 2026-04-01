---
ID: 020
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: platform, socialseed-validation-starter
---

# 020 - UsernameValidator regex inconsistency

## 1. Descripción Detallada
Existía una inconsistencia entre la regex definida en `UsernameRules.REGEX` y el patrón usado en `UsernameValidator.PATTERN`. `UsernameRules` permitía `.`, `_`, `-` mientras que `UsernameValidator` solo permitía `_`. Esto causaba comportamiento contradictorio.

**Archivos afectados:**
- `platform/socialseed-validation-starter/src/main/java/com/socialseed/validation/validator/UsernameRules.java`
- `platform/socialseed-validation-starter/src/main/java/com/socialseed/validation/validator/UsernameValidator.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Unificar ambas regex para que coincidan exactamente.
  - Eliminar una de las dos y usar solo una fuente de verdad.
- **Selección:** Se unificó la regex en `UsernameValidator.PATTERN` para que coincida con `UsernameRules.REGEX`. Mantiene ambas clases pero garantiza consistencia.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-validation-starter`.
- La validación es centralizada, no hay validadores ad-hoc en servicios.

## 4. Plan de Implementación y Testeo
- [x] Unificar regex entre UsernameRules y UsernameValidator
- [x] Verificar que validación funciona correctamente
- [x] Test unitario: verificar que usernames válidos se aceptan
- [x] Test unitario: verificar que usernames inválidos se rechazan

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Tener múltiples fuentes de verdad para validación es peligroso.
- Centralizar validación en el starter garantiza consistencia en todos los servicios.
- Esta corrección previene comportamientos inesperados en validación de usernames.
