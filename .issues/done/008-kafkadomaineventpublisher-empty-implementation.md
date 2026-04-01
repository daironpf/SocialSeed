---
ID: 008
Estado: hecha
Tipo: Feature
Prioridad: Alta
Impacto: socialuser-service
---

# 008 - KafkaDomainEventPublisher empty implementation

## 1. Descripción Detallada
La clase `KafkaDomainEventPublisher` en socialuser-service estaba completamente vacía (solo tenía un comentario `// implementa publisher`). Esto rompía la arquitectura basada en eventos, ya que los eventos de dominio no se publicaban a Kafka.

**Archivo afectado:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/infrastructure/messaging/kafka/KafkaDomainEventPublisher.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Implementar el publisher usando mensajes Proto definidos en socialseed-contracts.
  - Implementar el publisher con mensajes JSON simples.
- **Selección:** Se implementó con mensajes Proto. Mantiene consistencia con el resto del sistema, tipado fuerte, y compatibilidad con el esquema definido en contracts.

## 3. Restricciones de Arquitectura
- Se respeta la arquitectura hexagonal: la implementación está en `infrastructure.messaging.kafka`.
- Se usan mensajes Proto de `socialseed-contracts`.
- La interfaz del publisher permanece en el dominio.

## 4. Plan de Implementación y Testeo
- [x] Implementar KafkaDomainEventPublisher con KafkaTemplate
- [x] Usar mensajes Proto de socialseed-contracts
- [x] Configurar tópicos versionados con @Value
- [x] Test de integración: verificar que eventos se publican a Kafka

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los publishers vacíos son deuda técnica invisible que rompe la comunicación entre servicios.
- Usar Proto garantiza compatibilidad y tipado fuerte en eventos.
- Esta implementación habilita la arquitectura event-driven del sistema.
