---
ID: 053
Estado: hecha
Tipo: Refactor
Prioridad: Baja
Impacto: infrastructure, docker-compose
---

# 053 - Dockerfile build contexts standardized

## 1. Descripción Detallada
Los Dockerfiles de los servicios usaban contextos de build inconsistentes. api-gateway usaba contexto de servicio individual mientras que auth-service usaba contexto raíz.

**Archivos afectados:**
- `services/api-gateway/Dockerfile`
- `services/auth-service/Dockerfile`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Estandarizar todos los Dockerfiles a contexto raíz.
  - Estandarizar todos los Dockerfiles a contexto de servicio.
- **Selección:** Se estandarizó api-gateway a contexto raíz para coincidir con auth-service. Permite acceso a dependencias de plataforma durante el build.

## 3. Restricciones de Arquitectura
- Se mantiene el patrón multi-stage de Dockerfile.
- Compatible con build desde raíz del monorepo.

## 4. Plan de Implementación y Testeo
- [x] Estandarizar Dockerfile de api-gateway a contexto raíz
- [x] Verificar que docker build funciona correctamente
- [x] Test: verificar que imagen construida funciona

## 5. Lecciones y Justificación (Solo para issues en 'done')
- La consistencia en Dockerfiles facilita el mantenimiento y debugging.
- Esta corrección simplifica el proceso de build de imágenes.
