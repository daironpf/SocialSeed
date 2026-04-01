---
ID: 055
Estado: hecha
Tipo: Refactor
Prioridad: Baja
Impacto: platform, socialseed-api-response-starter
---

# 055 - Extra Spanish translation key removed

## 1. Descripción Detallada
El archivo `messages_es.properties` tenía una clave de traducción `user.username.size` que no existía en otros archivos de idioma. Esto causaba inconsistencia en la internacionalización.

**Archivo afectado:** `platform/socialseed-api-response-starter/src/main/resources/messages_es.properties`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Eliminar la clave extra de messages_es.properties.
  - Añadir la clave a todos los archivos de idioma.
- **Selección:** Se eliminó la clave extra de messages_es.properties. Mantiene consistencia entre idiomas.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-api-response-starter`.
- Las claves de traducción deben existir en todos los archivos de idioma.

## 4. Plan de Implementación y Testeo
- [x] Eliminar clave user.username.size de messages_es.properties
- [x] Verificar compilación
- [x] Verificar que no hay referencias a la clave eliminada

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Las claves de traducción inconsistentes causan mensajes faltantes en algunos idiomas.
- Esta corrección mantiene la consistencia de i18n.
