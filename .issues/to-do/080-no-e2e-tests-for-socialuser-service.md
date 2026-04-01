---
ID: 080
Estado: pendiente
Tipo: Feature
Prioridad: Media
Impacto: socialuser-service
---

# 080 - No E2E tests for socialuser-service

## 1. Descripción Detallada
Solo existen tests E2E para auth-service. El socialuser-service no tiene cobertura E2E para sus endpoints.

**Áreas sin cobertura:**
- Profile CRUD operations
- Vacation management
- gRPC integration tests

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Crear tests E2E con pytest + requests.
  - Usar el framework socialseed-e2e.
- **Selección:** Crear tests E2E con pytest + requests siguiendo el patrón establecido en testing-rules.md. Consistente con los tests existentes de auth-service.

## 3. Restricciones de Arquitectura
- Tests en `testing/tests/`.
- Usar requests (no Playwright) para APIs REST.
- Seguir testing-rules.md.

## 4. Plan de Implementación y Testeo
- [ ] Crear Page Object para socialuser-service
- [ ] Crear tests para profile CRUD
- [ ] Crear tests para vacation management
- [ ] Crear tests para gRPC integration
- [ ] Ejecutar suite E2E y verificar que pasa

## 5. Lecciones y Justificación (Solo para issues en 'done')
- [Pendiente]
