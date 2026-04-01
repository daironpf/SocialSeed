---
ID: 081
Estado: pendiente
Tipo: Feature
Prioridad: Media
Impacto: nexus-service
---

# 081 - No E2E tests for nexus service

## 1. Descripción Detallada
El servicio Nexus no tiene tests E2E a pesar de estar en desarrollo activo. Sin cobertura E2E, es difícil verificar que el servicio funciona correctamente en integración con otros servicios.

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Crear tests E2E con pytest + requests.
  - Crear tests de integración con Testcontainers.
- **Selección:** Crear tests E2E con pytest + requests siguiendo el patrón establecido. Consistente con otros tests del proyecto.

## 3. Restricciones de Arquitectura
- Tests en `testing/tests/`.
- Usar requests (no Playwright) para APIs REST.
- Seguir testing-rules.md.

## 4. Plan de Implementación y Testeo
- [ ] Crear Page Object para nexus-service
- [ ] Crear tests para endpoints principales
- [ ] Crear tests para integración con otros servicios
- [ ] Ejecutar suite E2E y verificar que pasa

## 5. Lecciones y Justificación (Solo para issues en 'done')
- [Pendiente]
