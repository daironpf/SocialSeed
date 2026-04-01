---
ID: 023
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: infrastructure, docker-compose
---

# 023 - api-gateway missing from docker-compose.yml

## 1. Descripción Detallada
El servicio api-gateway estaba definido en el `pom.xml` pero no existía en `docker-compose.yml`. Esto impedía desplegar el gateway como contenedor Docker.

**Archivo afectado:** `docker-compose.yml`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir api-gateway al docker-compose.yml con configuración completa.
  - Mantener api-gateway fuera de Docker y ejecutar nativamente.
- **Selección:** Se añadió api-gateway al docker-compose.yml con healthcheck, dependencias y variables de entorno. Completa el ecosistema de contenedores.

## 3. Restricciones de Arquitectura
- Se mantiene en la red `socialseed_net`.
- Depende de auth-service y socialuser-service.
- Expone puerto para acceso externo.

## 4. Plan de Implementación y Testeo
- [x] Añadir api-gateway service al docker-compose.yml
- [x] Configurar healthcheck
- [x] Configurar dependencias de servicios
- [x] Verificar que docker-compose up incluye api-gateway

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Todos los servicios deben estar en docker-compose para despliegue consistente.
- Esta corrección completa el ecosistema de contenedores.
