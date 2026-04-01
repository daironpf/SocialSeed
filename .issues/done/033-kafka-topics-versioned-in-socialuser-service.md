---
ID: 033
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: socialuser-service
---

# 033 - Kafka topics versioned in socialuser-service

## 1. Descripción Detallada
Los tópicos de Kafka en socialuser-service no estaban versionados. El `AuthEventsConsumer` usaba tópicos hardcodeados sin versión (ej. `auth.user.username.changed` en lugar de `auth.user.username.changed.v1`).

**Archivo afectado:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/entry/event/consumer/AuthEventsConsumer.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Versionar tópicos con sufijo .v1.
  - Usar configuración externalizada.
- **Selección:** Se versionaron con .v1 y se externalizaron con @Value. Sigue el estándar del proyecto.

## 3. Restricciones de Arquitectura
- Se mantiene en `entry.event.consumer`.
- Compatible con tópicos versionados de auth-service.

## 4. Plan de Implementación y Testeo
- [x] Versionar tópicos con sufijo .v1
- [x] Externalizar nombres con @Value
- [x] Verificar compilación
- [x] Test: verificar que consumer recibe eventos correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- El versionado de tópicos permite evolución de esquemas sin romper consumidores.
- Esta corrección alinea socialuser-service con los estándares de Kafka del proyecto.
