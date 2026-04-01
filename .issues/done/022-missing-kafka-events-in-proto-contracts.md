---
ID: 022
Estado: hecha
Tipo: Feature
Prioridad: Media
Impacto: platform, socialseed-contracts
---

# 022 - Missing Kafka events in proto contracts

## 1. Descripción Detallada
El archivo `auth_events.proto` en `socialseed-contracts` no definía los eventos de autenticación necesarios para la comunicación entre servicios. Faltaban 8 eventos críticos.

**Archivo afectado:** `platform/socialseed-contracts/src/main/proto/auth_events.proto`

**Eventos faltantes:**
- `AuthPasswordChanged`, `AuthPasswordReset`, `AuthAccountLocked`, `AuthAccountUnlocked`, `AuthUserLoggedIn`, `AuthUserLoggedOut`, `AuthRefreshTokenRevoked`, `AuthAllSessionsRevoked`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir los eventos faltantes al proto existente.
  - Crear un archivo proto separado para eventos de sesión.
- **Selección:** Se añadieron los 8 eventos faltantes a `auth_events.proto`. Mantiene todos los eventos de auth en un solo archivo para facilidad de mantenimiento.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-contracts`.
- Se sigue la nomenclatura de paquetes: `com.socialseed.contracts.auth`.
- Los eventos deben compilarse para generar clases Java.

## 4. Plan de Implementación y Testeo
- [x] Añadir 8 eventos faltantes a auth_events.proto
- [x] Compilar contracts: `mvn clean install -pl platform/socialseed-contracts -am`
- [x] Verificar que las clases Java se generan correctamente
- [x] Verificar que auth-service y socialuser-service consumen los nuevos eventos

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los contratos Proto son la fuente de verdad para comunicación gRPC/Kafka entre servicios.
- Mantener los contratos completos previene errores de serialización.
- Esta corrección habilita la comunicación completa de eventos de autenticación.
