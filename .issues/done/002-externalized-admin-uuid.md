---
ID: 002
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: auth-service
---

# 002 - Externalized admin UUID

## 1. Descripción Detallada
El `RoleController` en auth-service tenía un UUID de administrador hardcodeado (`00000000-0000-0000-0000-000000000001`) en las líneas 72 y 95. Esto representa un riesgo de seguridad y falta de flexibilidad para entornos de producción.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/entry/rest/controller/RoleController.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Usar `@Value("${security.admin.default-id}")` para inyectar desde configuración.
  - Crear un servicio de configuración dedicado para roles del sistema.
- **Selección:** Se usó `@Value` con propiedad de configuración. Es simple, sigue los estándares de Spring Boot, y permite override mediante variables de entorno en Docker.

## 3. Restricciones de Arquitectura
- Se mantiene en la capa `entry.rest.controller` donde pertenece.
- La configuración se externaliza siguiendo el patrón de perfiles de Spring Boot.
- No se introduce dependencia circular.

## 4. Plan de Implementación y Testeo
- [x] Reemplazar UUID hardcodeado con `@Value("${security.admin.default-id}")`
- [x] Agregar valor por defecto en `application.yml`
- [x] Verificar compilación
- [x] Test unitario: RoleController usa el UUID configurado

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Hardcoded IDs son un anti-patrón que dificulta la portabilidad entre entornos.
- Externalizar configuraciones sensibles mejora la seguridad y facilita el despliegue en diferentes ambientes.
