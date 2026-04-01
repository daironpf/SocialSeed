---
ID: 084
Estado: pendiente
Tipo: Feature
Prioridad: Baja
Impacto: testing
---

# 084 - No performance/load tests

## 1. Descripción Detallada
No existen tests de rendimiento o carga para escenarios de alto tráfico. Sin estos tests, es difícil saber cómo se comportará el sistema bajo carga.

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Usar k6 para tests de carga.
  - Usar JMeter para tests de rendimiento.
- **Selección:** Usar k6 para tests de carga. Es moderno, basado en JavaScript, y fácil de integrar en CI/CD.

## 3. Restricciones de Arquitectura
- Tests en `testing/performance/`.
- Usar k6 para tests de carga.
- No afectar tests existentes.

## 4. Plan de Implementación y Testeo
- [ ] Configurar k6 para tests de carga
- [ ] Crear tests para endpoints críticos (login, register)
- [ ] Crear tests para escenarios de alto tráfico
- [ ] Integrar k6 en CI/CD
- [ ] Ejecutar tests y analizar resultados

## 5. Lecciones y Justificación (Solo para issues en 'done')
- [Pendiente]
