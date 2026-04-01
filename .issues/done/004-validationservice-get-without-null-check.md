---
ID: 004
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: auth-service
---

# 004 - ValidationService.get() without null check

## 1. Descripción Detallada
En `ValidationService.java` línea 32, se usaba `.get()` directamente sobre un `Optional` retornado por `authUserRepository.findById(userId)`. Esto lanza `NoSuchElementException` si el usuario no existe, en lugar de manejar el caso adecuadamente.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/service/ValidationService.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Usar `.orElseThrow(() -> new BusinessException(...))` con mensaje de negocio.
  - Usar `.orElse(null)` y verificar manualmente (menos elegante).
- **Selección:** Se usó `.orElseThrow()` con `BusinessException`. Sigue el patrón de manejo de errores del proyecto y proporciona un mensaje claro al cliente.

## 3. Restricciones de Arquitectura
- Se mantiene en la capa de aplicación/servicio.
- Se usa `BusinessException` del error-handling starter.
- No se modifica el repositorio ni el dominio.

## 4. Plan de Implementación y Testeo
- [x] Reemplazar `.get()` con `.orElseThrow(() -> new BusinessException(...))`
- [x] Verificar compilación
- [x] Test unitario: lanzar BusinessException cuando userId no existe

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Usar `.get()` directamente sobre Optional es un anti-patrón en Java moderno.
- `.orElseThrow()` proporciona mejor control de errores y mensajes significativos.
- Fortalece la robustez del servicio de validación.
