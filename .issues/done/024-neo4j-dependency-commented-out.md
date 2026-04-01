---
ID: 024
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: infrastructure, docker-compose
---

# 024 - Neo4j dependency commented out in docker-compose

## 1. Descripción Detallada
La dependencia de Neo4j para socialuser-service estaba comentada en `docker-compose.yml`. Esto permitía que socialuser-service arrancara antes de que Neo4j estuviera listo, causando errores de conexión.

**Archivo afectado:** `docker-compose.yml` (líneas 31-33)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Descomentar la dependencia con condition: service_healthy.
  - Añadir retry logic en la aplicación.
- **Selección:** Se descomentó la dependencia con `condition: service_healthy`. Es la solución correcta de Docker Compose para garantizar orden de arranque.

## 3. Restricciones de Arquitectura
- Se mantiene en docker-compose.yml.
- Neo4j debe tener healthcheck definido.

## 4. Plan de Implementación y Testeo
- [x] Descomentar depends_on: neo4j con condition: service_healthy
- [x] Verificar que Neo4j tiene healthcheck
- [x] Verificar que docker-compose up arranca en orden correcto

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Las dependencias de servicios en Docker Compose son críticas para el arranque correcto.
- Esta corrección previene errores de conexión al inicio.
