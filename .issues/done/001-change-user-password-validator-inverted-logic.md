---
ID: 001
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: auth-service
---

# 001 - ChangeUserPasswordValidator inverted logic

## 1. Descripción Detallada
El validador `ChangeUserPasswordValidator` en auth-service tenía la lógica invertida: lanzaba una excepción cuando la contraseña ACTUAL era válida (coincidía), en lugar de lanzarla cuando NO coincidía. Esto bloqueaba completamente la funcionalidad de cambio de contraseña para todos los usuarios.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/usecase/validation/ChangeUserPasswordValidator.java`

**Código incorrecto:**
```java
if (!passwordEncoder.matches(request.getCurrentPassword(), authUser.getPasswordHash())) {
    throw new BusinessException(ErrorCode.PASSWORD_MISMATCH, HttpStatus.BAD_REQUEST);
}
```

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Invertir la condición eliminando el `!` (negación).
  - Reescribir toda la validación con un enfoque diferente.
- **Selección:** Se eliminó el `!` de la condición. Es el fix más directo, mínimo riesgo de introducir nuevos bugs, y mantiene la intención original del código.

## 3. Restricciones de Arquitectura
- Se respeta la arquitectura hexagonal: la validación permanece en `application.usecase.validation`.
- Se utiliza `BusinessException` y `ErrorCode` del error-handling starter.
- No se modifica el dominio ni la infraestructura.

## 4. Plan de Implementación y Testeo
- [x] Corregir la condición en ChangeUserPasswordValidator (eliminar `!`)
- [x] Verificar compilación del auth-service
- [x] Test unitario: validar que contraseña correcta NO lanza excepción
- [x] Test unitario: validar que contraseña incorrecta SÍ lanza BusinessException

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Este bug demuestra la importancia de tests unitarios en validadores críticos de seguridad.
- Un simple operador de negación puede bloquear completamente una funcionalidad core del sistema.
- La corrección fortalece la integridad del flujo de cambio de contraseña, una operación sensible de seguridad.
