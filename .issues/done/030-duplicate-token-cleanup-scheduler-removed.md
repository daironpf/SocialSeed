---
ID: 030
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: auth-service
---

# 030 - Duplicate TokenCleanupScheduler removed

## 1. Descripción Detallada
Existían dos mecanismos de limpieza de tokens: `TokenCleanupScheduler.java` y `TokenCleanupJob.java`, ambos ejecutándose en diferentes intervalos. Esto era redundante y causaba limpieza duplicada.

**Archivos afectados:**
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/scheduler/TokenCleanupScheduler.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/scheduler/TokenCleanupJob.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Eliminar TokenCleanupScheduler y mantener TokenCleanupJob.
  - Eliminar TokenCleanupJob y mantener TokenCleanupScheduler.
- **Selección:** Se eliminó TokenCleanupScheduler y se mantuvo TokenCleanupJob. Job es más específico y sigue mejores prácticas de scheduling.

## 3. Restricciones de Arquitectura
- Se mantiene en `infrastructure.scheduler`.
- No se modifica la lógica de limpieza.

## 4. Plan de Implementación y Testeo
- [x] Eliminar TokenCleanupScheduler.java
- [x] Verificar que TokenCleanupJob funciona correctamente
- [x] Verificar compilación

## 5. Lecciones y Justificación (Solo para issues en 'done')
- La duplicación de schedulers causa trabajo innecesario y confusión.
- Esta corrección simplifica la arquitectura de limpieza de tokens.
