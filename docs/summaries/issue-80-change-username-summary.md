# Issue 80: Change Username with Social Sync - Summary

## Overview
Implementación del caso de uso para el cambio de nombre de usuario (`ChangeUsername`) asegurando la consistencia entre el `auth-service` y el `socialuser-service`. El proceso incluye la actualización en la base de datos de identidad (PostgreSQL), la sincronización vía gRPC con el grafo social (Neo4j) y la emisión de eventos asíncronos vía Kafka para otros interesados.

## Changes Made

### Platform (Centralized Contracts)
- **socialuser.proto**: Se añadió el RPC `UpdateUsername` y los mensajes `UpdateUsernameRequest`/`UpdateUsernameResponse` para permitir la sincronización gRPC.

### Auth-Service
- **ChangeUsernameUseCase**: Implementación de la lógica de negocio que:
    1. Valida la existencia del usuario y la unicidad del nuevo username.
    2. Actualiza la base de datos PostgreSQL local.
    3. Sincroniza síncronamente con `socialuser-service` mediante gRPC.
    4. Emite un evento `AuthUserUsernameChanged` a Kafka (topic: `auth.user.username.changed`).
- **ChangeUsernameController**: Punto de entrada REST (`PATCH /auth/username`) que requiere autenticación.
- **ChangeUsernameRequestDTO**: Validación del nuevo nombre de usuario.
- **Optimización de Dependencias**: Migración a `spring-kafka` (3.3.1) para resolver conflictos de resolución de dependencias locales.

### SocialUser-Service
- **SocialUserNeo4jRepository**: Nueva consulta `@Query` para actualizar el atributo `username` en el nodo `SocialUser`.
- **ChangeUsername (UseCase)**: Lógica para actualizar el nodo en Neo4j, diseñada para ser idempotente.
- **SocialUserGrpcServiceImpl**: Implementación del método gRPC para recibir solicitudes de cambio desde `auth-service`.
- **AuthEventsConsumer**: Consumidor de Kafka que escucha cambios de username para asegurar la consistencia eventual si la sincronización gRPC fallara (redundancia).
- **Configuración Cloud**: Ajuste de `application-dev.yml` para conectar con Neo4j AuraDB, optimizando el uso de recursos locales.

## Verification Results
- **Registro/Login**: Exitoso.
- **Cambio de Username**: Exitoso.
- **Sync gRPC**: Verificado mediante logs de `socialuser-service`.
- **Sync Kafka**: Verificado mediante la recepción del evento en el consumidor.
- **Persistencia**: Confirmada la actualización en PostgreSQL y Neo4j AuraDB.
- **Nota**: E2E tests se implementan usando el framework externo preferido por el usuario.

## Conclusion
El sistema ahora maneja cambios de identidad de forma consistente a través de múltiples microservicios utilizando un enfoque híbrido de sincronización fuerte (gRPC) y consistencia eventual (Kafka).
