---
ID: 049
Estado: hecha
Tipo: Refactor
Prioridad: Baja
Impacto: platform, socialseed-validation-starter
---

# 049 - ValidRole missing braces in message fixed

## 1. Descripción Detallada
La anotación `@ValidRole` tenía un mensaje por defecto que usaba una clave i18n sin las llaves `{}` requeridas para la interpolación de mensajes de validación de Jakarta.

**Archivo afectado:** `platform/socialseed-validation-starter/src/main/java/com/socialseed/validation/annotation/ValidRole.java` (línea 14)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir llaves {} alrededor de la clave i18n.
  - Cambiar a mensaje directo.
- **Selección:** Se añadieron las llaves `{}` alrededor de la clave i18n. Sigue el estándar de Jakarta Validation.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-validation-starter`.
- Compatible con interpolación de mensajes de Jakarta.

## 4. Plan de Implementación y Testeo
- [x] Añadir llaves {} alrededor de clave i18n
- [x] Verificar compilación
- [x] Test: verificar que mensaje se resuelve correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Las llaves {} son requeridas para interpolación de mensajes en Jakarta Validation.
- Esta corrección asegura que los mensajes de error se resuelvan correctamente.
