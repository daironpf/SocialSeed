---
ID: 077
Estado: pendiente
Tipo: Feature
Prioridad: Media
Impacto: auth-service, socialuser-service
---

# 077 - No integration tests for Kafka producers

## 1. Descripción Detallada
No existen tests de integración para los producers de Kafka en auth-service y socialuser-service. No se verifica que los eventos se publiquen correctamente a Kafka.

**Áreas sin cobertura:**
- `KafkaUserRegisteredProducer` en auth-service
- `KafkaPasswordChangedProducer` en auth-service
- `KafkaDomainEventPublisher` en socialuser-service

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Usar Kafka embebido para tests de integración.
  - Mockear KafkaTemplate y verificar llamadas.
- **Selección:** Usar Kafka embebido (EmbeddedKafka) para tests de integración reales. Proporciona mayor confianza en la comunicación.

## 3. Restricciones de Arquitectura
- Tests en `src/test/java/`.
- Usar EmbeddedKafka de Spring Kafka.
- No requerir Kafka externo para tests.

## 4. Plan de Implementación y Testeo
- [ ] Configurar EmbeddedKafka para auth-service
- [ ] Crear tests para KafkaUserRegisteredProducer
- [ ] Crear tests para KafkaPasswordChangedProducer
- [ ] Configurar EmbeddedKafka para socialuser-service
- [ ] Crear tests para KafkaDomainEventPublisher
- [ ] Ejecutar tests y verificar que pasan

## 5. Lecciones y Justificación (Solo para issues en 'done')
- [Pendiente]
