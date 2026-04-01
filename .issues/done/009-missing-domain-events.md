---
ID: 009
Estado: hecha
Tipo: Feature
Prioridad: Alta
Impacto: socialuser-service
---

# 009 - Missing domain events

## 1. Descripción Detallada
No existía el paquete `domain/event` en socialuser-service. Sin eventos de dominio, no se podían comunicar cambios importantes del ciclo de vida del usuario social a otros servicios.

**Ubicación afectada:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/domain/event/` (no existía)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Crear eventos de dominio como records Java inmutables.
  - Usar clases con Lombok @Data.
- **Selección:** Se usaron `record` para inmutabilidad, siguiendo los estándares de codificación del proyecto. Se crearon 4 eventos: `SocialUserCreatedEvent`, `SocialUserProfileUpdatedEvent`, `SocialUserVacationStartedEvent`, `SocialUserVacationEndedEvent`.

## 3. Restricciones de Arquitectura
- Los eventos residen en `domain.event`, capa pura sin dependencias externas.
- Se usan records Java 21 para inmutabilidad.
- Los eventos se mapean a Proto en la capa de infraestructura antes de publicar.

## 4. Plan de Implementación y Testeo
- [x] Crear paquete domain/event
- [x] Crear SocialUserCreatedEvent record
- [x] Crear SocialUserProfileUpdatedEvent record
- [x] Crear SocialUserVacationStartedEvent record
- [x] Crear SocialUserVacationEndedEvent record
- [x] Verificar compilación
- [x] Test unitario: verificar que eventos contienen datos correctos

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los eventos de dominio son esenciales para la comunicación asíncrona entre microservicios.
- Usar records garantiza inmutabilidad y thread-safety.
- Esta creación habilita el patrón de eventos de dominio en la arquitectura hexagonal.
