---
ID: 036
Estado: hecha
Tipo: Feature
Prioridad: Media
Impacto: platform, socialseed-validation-starter
---

# 036 - Missing ValidEmail annotation created

## 1. Descripción Detallada
La validación de email usaba la anotación estándar `@Email` de Jakarta en lugar de una anotación propia del proyecto con soporte i18n consistente. Esto impedía tener mensajes de error centralizados y personalizados.

**Archivos creados:**
- `platform/socialseed-validation-starter/src/main/java/com/socialseed/validation/annotation/ValidEmail.java`
- `platform/socialseed-validation-starter/src/main/java/com/socialseed/validation/validator/EmailValidator.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Crear anotación @ValidEmail propia con validador.
  - Mantener @Email de Jakarta con mensaje personalizado.
- **Selección:** Se creó @ValidEmail con EmailValidator. Mantiene consistencia con otros validadores del starter y permite i18n.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-validation-starter`.
- El validador implementa `ConstraintValidator`.
- Mensaje i18n centralizado en messages.properties.

## 4. Plan de Implementación y Testeo
- [x] Crear anotación @ValidEmail
- [x] Crear EmailValidator
- [x] Agregar clave i18n en messages.properties
- [x] Verificar compilación
- [x] Test unitario: verificar que validación funciona

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Validadores propios permiten mensajes i18n y comportamiento consistente.
- Esta creación completa el conjunto de validadores del proyecto.
