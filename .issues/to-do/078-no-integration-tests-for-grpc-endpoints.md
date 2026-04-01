---
ID: 078
Estado: pendiente
Tipo: Feature
Prioridad: Media
Impacto: socialuser-service
---

# 078 - No integration tests for gRPC endpoints

## 1. Descripción Detallada
Los endpoints gRPC del socialuser-service no tienen tests de integración. No se verifica que la comunicación gRPC funcione correctamente.

**Áreas sin cobertura:**
- gRPC server en socialuser-service
- gRPC client en auth-service

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Usar gRPC embebido para tests de integración.
  - Mockear el cliente gRPC.
- **Selección:** Usar gRPC embebido (InProcessServerBuilder) para tests de integración reales. Proporciona mayor confianza en la comunicación entre servicios.

## 3. Restricciones de Arquitectura
- Tests en `src/test/java/`.
- Usar InProcessServerBuilder de gRPC.
- No requerir servidor gRPC externo para tests.

## 4. Plan de Implementación y Testeo
- [ ] Configurar gRPC embebido para tests
- [ ] Crear tests para endpoints gRPC de socialuser-service
- [ ] Crear tests para cliente gRPC de auth-service
- [ ] Ejecutar tests y verificar que pasan

## 5. Lecciones y Justificación (Solo para issues en 'done')
- [Pendiente]
