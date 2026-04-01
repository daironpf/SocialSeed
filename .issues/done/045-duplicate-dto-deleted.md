---
ID: 045
Estado: hecha
Tipo: Refactor
Prioridad: Baja
Impacto: auth-service
---

# 045 - Duplicate DTO deleted

## 1. Descripción Detallada
Existía un DTO duplicado `PasswordChangeRequest.java` que tenía campos similares a `ChangePasswordRequestDTO.java`. Esto causaba confusión y código redundante.

**Archivos afectados:**
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/entry/rest/dto/request/PasswordChangeRequest.java` (eliminado)
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/entry/rest/dto/request/ChangePasswordRequestDTO.java` (mantenido)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Eliminar PasswordChangeRequest.java y mantener ChangePasswordRequestDTO.java.
  - Fusionar ambos DTOs en uno solo.
- **Selección:** Se eliminó PasswordChangeRequest.java. Mantiene el DTO con mejor nomenclatura (sigue el patrón *RequestDTO).

## 3. Restricciones de Arquitectura
- Se mantiene en `entry.rest.dto.request`.
- Se usa record Java 21 para el DTO mantenido.

## 4. Plan de Implementación y Testeo
- [x] Eliminar PasswordChangeRequest.java
- [x] Verificar que no hay referencias al DTO eliminado
- [x] Verificar compilación
- [x] Test: verificar que cambio de contraseña funciona con DTO correcto

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los DTOs duplicados causan confusión y mantenimiento doble.
- Esta corrección simplifica la base de código.
