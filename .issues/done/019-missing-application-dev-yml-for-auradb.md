---
ID: 019
Estado: hecha
Tipo: Feature
Prioridad: Media
Impacto: socialuser-service
---

# 019 - Missing application-dev.yml for AuraDB

## 1. Descripción Detallada
El socialuser-service no tenía un archivo `application-dev.yml` configurado para conectarse a la instancia de Neo4j AuraDB en la nube. Sin este perfil, no era posible ejecutar el servicio nativamente con Maven conectado a Neo4j Cloud.

**Archivo afectado:** `services/socialuser-service/src/main/resources/application-dev.yml` (no existía)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Crear application-dev.yml con credenciales de AuraDB.
  - Usar Neo4j local en Docker para desarrollo.
- **Selección:** Se creó application-dev.yml con configuración de AuraDB. Ahora los recursos locales al ejecutar nativamente con Maven.

## 3. Restricciones de Arquitectura
- El archivo está en resources, ignorado por Git (.gitignore).
- Compatible con perfil `dev` para desarrollo nativo.
- El perfil `docker` usa Neo4j en contenedor.

## 4. Plan de Implementación y Testeo
- [x] Crear application-dev.yml con URI de AuraDB
- [x] Configurar credenciales de Neo4j Cloud
- [x] Verificar que socialuser-service arranca con perfil dev
- [x] Test: verificar conexión a AuraDB

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los perfiles de Spring Boot permiten flexibilidad entre entornos.
- AuraDB Cloud ahorra recursos locales en máquinas de desarrollo.
- Esta configuración habilita el desarrollo nativo sin Docker para Neo4j.
