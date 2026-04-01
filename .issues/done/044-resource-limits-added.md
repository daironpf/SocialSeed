---
ID: 044
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: infrastructure, docker-compose
---

# 044 - Resource limits added to docker-compose

## 1. Descripción Detallada
No había límites de memoria o CPU configurados para los servicios en `docker-compose.yml`. Esto podía causar consumo excesivo de recursos y potencial DoS.

**Archivo afectado:** `docker-compose.yml`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir límites de memoria con deploy.resources.limits.
  - Usar limits de Docker Compose v2.
- **Selección:** Se añadieron límites de memoria (512M) a todos los servicios usando `deploy.resources.limits.memory`. Previene consumo excesivo de recursos.

## 3. Restricciones de Arquitectura
- Se mantiene en docker-compose.yml.
- Los límites son razonables para servicios Spring Boot.

## 4. Plan de Implementación y Testeo
- [x] Añadir memory limits a todos los servicios
- [x] Verificar que docker-compose up funciona con límites
- [x] Test: verificar que servicios no exceden memoria asignada

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los límites de recursos previenen que un servicio consuma todos los recursos.
- Esta corrección mejora la estabilidad del ecosistema Docker.
