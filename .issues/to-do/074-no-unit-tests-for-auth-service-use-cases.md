---
ID: 074
Estado: pendiente
Tipo: Feature
Prioridad: Alta
Impacto: auth-service
---

# 074 - No unit tests for auth-service use cases

## 1. Descripción Detallada
No existen tests unitarios para los use cases del auth-service. Los casos de uso críticos como `RegisterUser`, `LoginUser`, `ChangePassword`, `ChangeUsername`, `GetUserById`, `RefreshToken` y `Logout` no tienen cobertura de tests.

**Use cases sin cobertura:**
- `RegisterUser`
- `LoginUser`
- `ChangePassword`
- `ChangeUsername`
- `GetUserById`
- `RefreshToken`
- `Logout`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Crear tests unitarios con JUnit 5 + Mockito para cada use case.
  - Crear tests de integración con Testcontainers.
- **Selección:** Tests unitarios con Mockito para cada use case. Son rápidos y no requieren infraestructura externa.

## 3. Restricciones de Arquitectura
- Tests en `src/test/java/` siguiendo estructura de paquetes.
- Usar Mockito para mockear repositorios y servicios.
- Usar AssertJ para aserciones.

## 4. Plan de Implementación y Testeo
- [ ] Crear RegisterUserTest
- [ ] Crear LoginUserTest
- [ ] Crear ChangePasswordTest
- [ ] Crear ChangeUsernameTest
- [ ] Crear GetUserByIdTest
- [ ] Crear RefreshTokenTest
- [ ] Crear LogoutTest
- [ ] Ejecutar `mvn test -pl services/auth-service`

## 5. Lecciones y Justificación (Solo para issues en 'done')
- [Pendiente]
