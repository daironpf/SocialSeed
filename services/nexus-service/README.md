# SocialSeed Nexus Service 🛰️

**Nexus** es el centro de mando y orquestación administrativa de la plataforma SocialSeed. Actúa como el puente crítico entre la toma de decisiones (Gobernanza) y la ejecución técnica (Auth/Data).

## 🚀 Propósito
Mientras otros servicios analizan o almacenan, **Nexus ejecuta**. Su función es garantizar que las acciones administrativas (baneos, cierres de sesión forzados, cambios de roles) se realicen de forma atómica, segura y totalmente auditada en toda la malla de microservicios.

## 🛠️ Capacidades Core
- **Orquestación de Identidad:** Ejecuta cambios de estado en usuarios comunicándose directamente con `auth-service` vía gRPC.
- **Sesiones en Tiempo Real:** Interactúa con capas de caché (Redis) para realizar *Force Logout* instantáneos.
- **Propagación de Eventos:** Emite eventos críticos a través de **Apache Kafka** para sincronizar el estado del usuario en todos los módulos del sistema.
- **Logs de Auditoría Inmutables:** Registro detallado de cada acción administrativa para garantizar la transparencia institucional.

## 🏗️ Arquitectura
- **Framework:** Spring Boot 3.4 / Java 21
- **Comunicación:** gRPC (Interna) + REST (Admin UI)
- **Mensajería:** Kafka (Event-Driven State Propagation)
- **Patrón:** Orchestration Pattern

## 📖 Contrato gRPC
El servicio expone `AdminAuthService`, permitiendo:
- `rpc ForceLogoutUser`: Invalida sesiones activas en Redis.
- `rpc UpdateUserAccess`: Modifica bits de baneo o suspensión en la base de datos de identidad.