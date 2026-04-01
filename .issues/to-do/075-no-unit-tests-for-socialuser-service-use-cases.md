---
ID: 075
Estado: pendiente
Tipo: Feature
Prioridad: Alta
Impacto: socialuser-service
---

# 075 - No unit tests for socialuser-service use cases

## 1. Descripción Detallada
No existen tests unitarios para los use cases del socialuser-service. Todos los casos de uso carecen de cobertura de tests.

**Use cases sin cobertura:**
- `CreateUser`
- `GetUserById`
- `GetUserByEmail`
- `GetUserByName`
- `GetAllUsers`
- `DeleteUser`
- `UpdateUserProfile`
- `StartVacation`
- `EndVacation`
- `ChangeUsername`
- `ChangeEmail`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Crear tests unitarios con JUnit 5 + Mockito para cada use case.
  - Crear tests de integración con Testcontainers.
- **Selección:** Tests unitarios con Mockito para cada use case. Rápidos y sin infraestructura externa.

## 3. Restricciones de Arquitectura
- Tests en `src/test/java/` siguiendo estructura de paquetes.
- Usar Mockito para mockear repositorios Neo4j.
- Usar AssertJ para aserciones.

## 4. Plan de Implementación y Testeo
- [ ] Crear CreateUserTest
- [ ] Crear GetUserByIdTest
- [ ] Crear GetUserByEmailTest
- [ ] Crear GetUserByNameTest
- [ ] Crear GetAllUsersTest
- [ ] Crear DeleteUserTest
- [ ] Crear UpdateUserProfileTest
- [ ] Crear StartVacationTest
- [ ] Crear EndVacationTest
- [ ] Crear ChangeUsernameTest
- [ ] Crear ChangeEmailTest
- [ ] Ejecutar `mvn test -pl services/socialuser-service`

## 5. Lecciones y Justificación (Solo para issues en 'done')
- [Pendiente]
