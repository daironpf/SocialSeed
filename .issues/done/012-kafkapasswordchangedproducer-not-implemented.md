---
ID: 012
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: auth-service
---

# 012 - KafkaPasswordChangedProducer not implemented

## 1. Descripción Detallada
El `KafkaPasswordChangedProducer` en auth-service tenía toda la lógica de envío comentada. El método `publish()` solo hacía log, no enviaba eventos reales a Kafka. Esto impedía que otros servicios recibieran notificaciones de cambios de contraseña.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/messaging/kafka/KafkaPasswordChangedProducer.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Descomentar y corregir la lógica de envío con KafkaTemplate.
  - Reimplementar desde cero usando el patrón del KafkaDomainEventPublisher.
- **Selección:** Se implementó con KafkaTemplate y mensajes Proto, siguiendo el patrón establecido en socialseed-contracts. Mantiene consistencia con otros producers.

## 3. Restricciones de Arquitectura
- Se respeta la arquitectura hexagonal: implementación en `infrastructure.messaging.kafka`.
- Se usan mensajes Proto de `socialseed-contracts`.
- Tópicos versionados con `.v1`.

## 4. Plan de Implementación y Testeo
- [x] Implementar lógica de envío con KafkaTemplate
- [x] Usar mensajes Proto AuthPasswordChanged
- [x] Configurar tópico versionado
- [x] Test de integración: verificar que evento se publica correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los producers comentados son deuda técnica que rompe la comunicación entre servicios.
- Esta implementación permite que otros servicios reaccionen a cambios de contraseña.
