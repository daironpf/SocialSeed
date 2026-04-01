---
ID: 025
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: auth-service, infrastructure
---

# 025 - Redis dependency missing for auth-service in docker-compose

## 1. Descripción Detallada
El auth-service requiere Redis para la blacklist de tokens pero no tenía una dependencia definida en `docker-compose.yml`. Esto permitía que auth-service arrancara antes de que Redis estuviera listo.

**Archivo afectado:** `docker-compose.yml`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir depends_on: redis con condition: service_healthy.
  - Añadir retry logic en la aplicación para reconexión.
- **Selección:** Se añadió `depends_on: redis` con `condition: service_healthy`. Es la solución estándar de Docker Compose.

## 3. Restricciones de Arquitectura
- Se mantiene en docker-compose.yml.
- Redis debe tener healthcheck definido.

## 4. Plan de Implementación y Testeo
- [x] Añadir depends_on: redis a auth-service en docker-compose.yml
- [x] Verificar que Redis tiene healthcheck
- [x] Verificar que docker-compose up arranca en orden correcto

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Las dependencias de infraestructura deben estar explícitas en Docker Compose.
- Esta corrección previene errores de conexión a Redis al inicio.
