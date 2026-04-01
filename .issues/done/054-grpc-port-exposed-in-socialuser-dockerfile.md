---
ID: 054
Estado: hecha
Tipo: Bug
Prioridad: Baja
Impacto: socialuser-service, infrastructure
---

# 054 - gRPC port exposed in socialuser-service Dockerfile

## 1. Descripción Detallada
El Dockerfile de socialuser-service solo exponía el puerto 8090 (REST) pero no el puerto 9090 (gRPC). Esto impedía la comunicación gRPC cuando el servicio se ejecutaba en Docker.

**Archivo afectado:** `services/socialuser-service/Dockerfile`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir EXPOSE 9090 al Dockerfile.
  - Mapear puerto en docker-compose.yml sin EXPOSE.
- **Selección:** Se añadió `EXPOSE 9090` al Dockerfile. Documenta el puerto gRPC y facilita el mapeo en docker-compose.

## 3. Restricciones de Arquitectura
- Se mantiene en el Dockerfile del servicio.
- Compatible con docker-compose.yml existente.

## 4. Plan de Implementación y Testeo
- [x] Añadir EXPOSE 9090 al Dockerfile
- [x] Verificar que docker build funciona
- [x] Test: verificar que gRPC es accesible desde otros contenedores

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los Dockerfiles deben exponer todos los puertos que usa el servicio.
- Esta corrección habilita la comunicación gRPC en Docker.
