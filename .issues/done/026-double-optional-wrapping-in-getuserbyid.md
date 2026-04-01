---
ID: 026
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: auth-service
---

# 026 - Double Optional wrapping in GetUserById

## 1. Descripción Detallada
El use case `GetUserById` envolvía innecesariamente un `Optional` dentro de otro `Optional` usando `Optional.ofNullable(repository.findById(...))`. Como `findById` ya retorna `Optional`, esto creaba un `Optional<Optional<T>>` innecesario.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/usecase/GetUserById.java` (línea 21)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Eliminar el `Optional.ofNullable` wrapper redundante.
  - Mantener el wrapper pero con lógica adicional.
- **Selección:** Se eliminó el `Optional.ofNullable` redundante. Es el fix más limpio y correcto.

## 3. Restricciones de Arquitectura
- Se mantiene en la capa de aplicación/usecase.
- No se modifica el repositorio ni el dominio.

## 4. Plan de Implementación y Testeo
- [x] Eliminar Optional.ofNullable wrapper redundante
- [x] Verificar compilación
- [x] Test unitario: verificar que retorna Optional<User> correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Envolver Optional en Optional es un anti-patrón que complica el código innecesariamente.
- Esta corrección simplifica el código y mejora la legibilidad.
