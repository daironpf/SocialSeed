---
ID: 041
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: infrastructure, docker-compose
---

# 041 - Healthchecks added to docker-compose

## 1. Descripción Detallada
Los servicios api-gateway y auth-service no tenían healthchecks definidos en `docker-compose.yml`. Esto impedía que Docker verificara su estado de salud y gestionara correctamente las dependencias.

**Archivo afectado:** `docker-compose.yml`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir healthchecks HTTP a los servicios faltantes.
  - Usar healthchecks TCP simples.
- **Selección:** Se añadieron healthchecks HTTP usando el endpoint `/actuator/health` de Spring Boot. Es el estándar de Spring Boot y proporciona información detallada del estado.

## 3. Restricciones de Arquitectura
- Se mantiene en docker-compose.yml.
- Los healthchecks usan el endpoint de Spring Boot Actuator.

## 4. Plan de Implementación y Testeo
- [x] Añadir healthcheck HTTP a api-gateway
- [x] Añadir healthcheck HTTP a auth-service
- [x] Verificar que docker-compose muestra estado healthy
- [x] Test: verificar que healthchecks responden correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los healthchecks son esenciales para la orquestación de contenedores.
- Esta corrección mejora la fiabilidad del ecosistema Docker.
