---
ID: 029
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: auth-service
---

# 029 - Inconsistent Kafka topic names versioned

## 1. Descripción Detallada
Los tópicos de Kafka en auth-service no seguían un patrón consistente de versionado. Algunos usaban nombres hardcodeados sin versión, otros usaban configuración. Esto dificultaba la evolución del esquema de mensajes.

**Archivos afectados:**
- `KafkaUserRegisteredProducer.java` - hardcodeado `"auth.user.registered"`
- `application.yml` - config usa `kafka.topic.auth-user-registered`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Versionar todos los tópicos con sufijo `.v1`.
  - Usar configuración centralizada para nombres de tópicos.
- **Selección:** Se versionaron todos los tópicos con `.v1` y se usó `@Value` para obtener nombres de configuración. Combina versionado con externalización.

## 3. Restricciones de Arquitectura
- Se mantiene en `infrastructure.messaging.kafka`.
- Los tópicos versionados permiten evolución de esquemas.

## 4. Plan de Implementación y Testeo
- [x] Versionar tópicos con sufijo .v1
- [x] Externalizar nombres con @Value
- [x] Verificar compilación
- [x] Test: verificar que eventos se publican a tópicos correctos

## 5. Lecciones y Justificación (Solo para issues en 'done')
- El versionado de tópicos es esencial para la evolución de esquemas Kafka.
- Esta corrección facilita el mantenimiento futuro de eventos.
