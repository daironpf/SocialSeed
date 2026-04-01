---
ID: 059
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: infrastructure, docker-compose
---

# 059 - Hardcoded credentials in docker-compose externalized (SEC-002)

## 1. Descripción Detallada
El `docker-compose.yml` tenía credenciales hardcodeadas para Neo4j (`neo4j/neoSocial`) y PostgreSQL (`authuser/authpass`). Esto representaba un riesgo de seguridad al tener credenciales en control de versiones.

**Archivo afectado:** `docker-compose.yml`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Usar variables de entorno para todas las credenciales.
  - Usar Docker secrets.
- **Selección:** Se externalizaron credenciales a variables de entorno. Simple y compatible con Docker Compose sin infraestructura adicional.

## 3. Restricciones de Arquitectura
- Se mantiene en docker-compose.yml.
- Las variables de entorno se definen en .env (ignorado por git).

## 4. Plan de Implementación y Testeo
- [x] Reemplazar credenciales hardcodeadas con variables de entorno
- [x] Crear archivo .env de ejemplo
- [x] Verificar que docker-compose up funciona
- [x] Test: verificar que servicios se conectan a bases de datos

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Las credenciales nunca deben estar en docker-compose.yml.
- Esta corrección mejora la seguridad del proyecto.
