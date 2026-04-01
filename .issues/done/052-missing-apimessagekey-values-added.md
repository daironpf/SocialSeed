---
ID: 052
Estado: hecha
Tipo: Feature
Prioridad: Baja
Impacto: platform, socialseed-api-response-starter
---

# 052 - Missing ApiMessageKey values added

## 1. Descripción Detallada
El enum `ApiMessageKey` no tenía valores para casos comunes de respuesta: UPDATED, DELETED, BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, CONFLICT, INTERNAL_ERROR.

**Archivo afectado:** `platform/socialseed-api-response-starter/src/main/java/com/socialseed/apiresponse/ApiMessageKey.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir todos los valores faltantes al enum existente.
  - Crear enums separados por categoría.
- **Selección:** Se añadieron todos los valores faltantes al enum existente. Mantiene un solo punto de referencia para mensajes.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-api-response-starter`.
- Cada valor tiene su clave i18n correspondiente en messages.properties.

## 4. Plan de Implementación y Testeo
- [x] Añadir UPDATED, DELETED, BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, CONFLICT, INTERNAL_ERROR
- [x] Agregar claves i18n correspondientes
- [x] Verificar compilación
- [x] Test: verificar que cada valor resuelve su mensaje correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Un enum completo de mensajes cubre todos los casos de uso comunes.
- Esta corrección elimina la necesidad de mensajes hardcodeados.
