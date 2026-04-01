---
ID: 079
Estado: pendiente
Tipo: Refactor
Prioridad: Media
Impacto: testing
---

# 079 - E2E tests directory structure

## 1. Descripción Detallada
Los tests E2E existen en `testing/tests/` pero el framework `socialseed-e2e` espera el directorio `services/`. Hay una inconsistencia en la estructura de directorios de tests.

**Problema:** Los tests E2E están en `testing/` pero el framework espera `services/`.

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Mover tests a `services/auth/services/` para el framework socialseed-e2e.
  - Mantener como tests pytest (enfoque actual funciona para REST APIs).
- **Selección:** Mantener como tests pytest en `testing/`. El enfoque actual funciona bien para APIs REST y no requiere migración costosa.

## 3. Restricciones de Arquitectura
- Tests en `testing/tests/`.
- Usar pytest con requests (no Playwright para REST).
- Seguir testing-rules.md.

## 4. Plan de Implementación y Testeo
- [ ] Documentar estructura actual de tests E2E
- [ ] Verificar que tests pytest funcionan correctamente
- [ ] Actualizar documentación si es necesario
- [ ] Ejecutar suite E2E y verificar que pasa

## 5. Lecciones y Justificación (Solo para issues en 'done')
- [Pendiente]
