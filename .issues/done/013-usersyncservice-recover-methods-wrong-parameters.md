---
ID: 013
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: auth-service
---

# 013 - UserSyncService @Recover methods wrong parameter names

## 1. Descripción Detallada
Los métodos `@Recover` en `UserSyncService` tenían nombres de parámetros incorrectos (`oldVal`, `newVal` en lugar de `oldUsername`, `newUsername`). Esto causaba confusión y posibles errores de lógica al recuperar de fallos de circuit breaker.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/service/UserSyncService.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Renombrar parámetros para coincidir con los del método original.
  - Separar en métodos @Recover independientes por tipo de operación.
- **Selección:** Se separaron en métodos `@Recover` independientes con nombres de parámetros correctos. Es más claro y evita ambigüedades.

## 3. Restricciones de Arquitectura
- Se mantiene en `infrastructure.service`, capa de adaptadores de salida.
- Se respeta la firma requerida por `@Recover` de Resilience4j.

## 4. Plan de Implementación y Testeo
- [x] Separar métodos @Recover por tipo de operación
- [x] Corregir nombres de parámetros
- [x] Verificar compilación
- [x] Test: verificar que recovery funciona correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los métodos @Recover deben coincidir exactamente con la firma del método original.
- Métodos separados son más claros y mantenibles que uno genérico.
