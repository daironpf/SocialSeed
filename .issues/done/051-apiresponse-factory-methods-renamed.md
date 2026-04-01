---
ID: 051
Estado: hecha
Tipo: Refactor
Prioridad: Baja
Impacto: platform, socialseed-api-response-starter
---

# 051 - ApiResponse factory methods renamed for consistency

## 1. Descripción Detallada
Los métodos factory de `ApiResponse` tenían patrones inconsistentes: algunos usaban `success` y otros `message`. Esto causaba confusión al usar la clase.

**Archivo afectado:** `platform/socialseed-api-response-starter/src/main/java/com/socialseed/apiresponse/ApiResponse.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Renombrar `message(ApiMessageKey)` a `success(ApiMessageKey)`.
  - Mantener ambos como aliases.
- **Selección:** Se renombró `message(ApiMessageKey)` a `success(ApiMessageKey)`. Mantiene consistencia con el patrón de nombres de ApiResponse.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-api-response-starter`.
- No se cambia la firma de los métodos, solo el nombre.

## 4. Plan de Implementación y Testeo
- [x] Renombrar método message a success
- [x] Actualizar todas las referencias en servicios
- [x] Verificar compilación
- [x] Test: verificar que respuestas se construyen correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- La consistencia en nombres de métodos mejora la ergonomía del API.
- Esta corrección simplifica el uso de ApiResponse en todos los servicios.
