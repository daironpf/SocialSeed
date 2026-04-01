---
ID: 050
Estado: hecha
Tipo: Refactor
Prioridad: Baja
Impacto: platform, socialseed-api-response-starter
---

# 050 - ResponseDTO deprecated as redundant

## 1. Descripción Detallada
Existían dos clases de respuesta: `ApiResponse<T>` y `ResponseDTO`. `ResponseDTO` era redundante y tenía menos funcionalidades que `ApiResponse`.

**Archivo afectado:** `platform/socialseed-api-response-starter/src/main/java/com/socialseed/apiresponse/ResponseDTO.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Deprecar ResponseDTO y migrar todo a ApiResponse.
  - Eliminar ResponseDTO completamente.
- **Selección:** Se deprecó ResponseDTO con `@Deprecated`. Permite migración gradual sin romper código existente.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-api-response-starter`.
- Se marca como deprecated para migración futura.

## 4. Plan de Implementación y Testeo
- [x] Añadir @Deprecated a ResponseDTO
- [x] Añadir JavaDoc explicando migración a ApiResponse
- [x] Verificar compilación
- [x] Verificar que servicios existentes siguen funcionando

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Las clases redundantes causan confusión y mantenimiento doble.
- Deprecar permite migración gradual sin breaking changes.
- Esta corrección simplifica la API del starter.
