# Issue 57: Robust Sync for Auth User Data (Email Changes) - Summary

## Overview
Implementación de un mecanismo robusto de sincronización para cambios de nombre de usuario y correo electrónico. Se ha introducido una capa de resiliencia con políticas de reintento y un mecanismo de recuperación para garantizar la consistencia entre el servicio de identidad (Auth) y el grafo social (SocialUser).

## Changes Made

### Platform (Centralized Contracts)
- **socialuser.proto**: Se añadió el RPC `UpdateEmail` y los mensajes `UpdateEmailRequest`/`UpdateEmailResponse`.
- **auth_events.proto**: Se añadió el mensaje `AuthUserEmailChanged` para notificaciones asíncronas vía Kafka.

### Auth-Service
- **UserSyncService**: Nuevo servicio centralizado de sincronización que:
    - Utiliza **Spring Retry** para reintentos exponenciales (máximo 3 intentos, retraso inicial de 1s con multiplicador x2).
    - Realiza llamadas síncronas vía gRPC para asegurar consistencia inmediata.
    - Emite eventos Kafka para consistencia eventual y desacoplamiento.
    - Implementa lógica de recuperación (`@Recover`): si los reintentos fallan, el error se registra y se envía un mensaje al tópico `auth.user.sync.failures` para inspección manual futura.
- **Refactorización de Use Cases**:
    - `ChangeUsernameUseCase`: Ahora utiliza `UserSyncService` para manejar el proceso de sincronización de forma resiliente.
    - `VerifyEmailChange`: Se actualizó para disparar la sincronización robusta una vez que el usuario verifica su nueva dirección de correo.
- **Configuración**: Habilitación de `@EnableRetry` en la aplicación.

### SocialUser-Service
- **Repositorio Neo4j**: Añadida la consulta idempotente `updateEmail`.
- **Caso de Uso**: Nuevo `ChangeEmail` para la actualización del nodo `SocialUser`.
- **gRPC Service**: Implementación del handler `updateEmail`.
- **Kafka Consumer**: Añadido `consumeEmailChanged` para redundancia y consistencia eventual.

## Verification Results

### Unit/Integration Testing
Se creó el test `UserSyncServiceRetryTest` que verifica:
1. **Éxito tras reintento**: Simula fallos temporales de gRPC y confirma que el servicio reintenta y finalmente tiene éxito.
2. **Recuperación tras agotamiento**: Simula un fallo persistente y confirma que, tras agotar los reintentos, el sistema registra el error y envía los datos al tópico de fallos.

### Flow Testing
- `EmailChangeIntegrationTest`: Confirmó que el flujo completo de cambio de email (solicitud + verificación) sigue funcionando correctamente con la nueva capa de sincronización integrada.

## Conclusion
El sistema ahora cuenta con una arquitectura de sincronización resiliente que minimiza las inconsistencias entre servicios y proporciona herramientas de observación (Dead Letter Topic) para fallos críticos que requieren intervención manual.
