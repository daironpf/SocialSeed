---
ID: 035
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: platform, socialseed-validation-starter
---

# 035 - ValidUsername hardcoded message changed to i18n

## 1. Descripción Detallada
La anotación `@ValidUsername` tenía un mensaje por defecto hardcodeado en español en lugar de usar una clave i18n del api-response-starter. Esto impedía la internacionalización consistente.

**Archivo afectado:** `platform/socialseed-validation-starter/src/main/java/com/socialseed/validation/annotation/ValidUsername.java` (línea 15)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Cambiar mensaje hardcodeado a clave i18n.
  - Mantener mensaje en español como fallback.
- **Selección:** Se cambió a clave i18n `{validation.username.invalid}`. Permite internacionalización y mantiene consistencia con otros validadores.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-validation-starter`.
- La clave i18n se define en `messages.properties` del api-response-starter.

## 4. Plan de Implementación y Testeo
- [x] Cambiar mensaje hardcodeado a clave i18n
- [x] Agregar clave en messages.properties
- [x] Verificar compilación
- [x] Test: verificar que mensaje se resuelve correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los mensajes hardcodeados impiden internacionalización.
- Centralizar mensajes en el starter garantiza consistencia.
- Esta corrección habilita i18n para validación de usernames.
