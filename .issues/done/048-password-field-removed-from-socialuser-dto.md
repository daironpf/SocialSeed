---
ID: 048
Estado: hecha
Tipo: Refactor
Prioridad: Baja
Impacto: socialuser-service
---

# 048 - Password field removed from socialuser DTO

## 1. Descripción Detallada
El `UserCreateRequestDTO` en socialuser-service contenía un campo `password` que no debía estar allí. La contraseña es responsabilidad del auth-service, no del socialuser-service.

**Archivo afectado:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/entry/rest/dto/request/UserCreateRequestDTO.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Eliminar el campo password del DTO.
  - Mantener el campo pero ignorarlo.
- **Selección:** Se eliminó el campo password. El socialuser-service no debe manejar contraseñas; eso es responsabilidad del auth-service.

## 3. Restricciones de Arquitectura
- Se mantiene en `entry.rest.dto.request`.
- Se respeta la separación de responsabilidades entre servicios.

## 4. Plan de Implementación y Testeo
- [x] Eliminar campo password de UserCreateRequestDTO
- [x] Verificar compilación
- [x] Test: verificar que creación de usuario social funciona sin password

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Cada servicio debe manejar solo los datos de su dominio.
- Esta corrección refuerza la separación de responsabilidades entre microservicios.
