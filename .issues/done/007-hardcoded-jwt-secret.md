---
ID: 007
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: auth-service, platform
---

# 007 - Hardcoded JWT secret

## 1. Descripción Detallada
El secreto JWT estaba hardcodeado en `application.yml` del auth-service. Esto representa un riesgo de seguridad crítico ya que el secreto queda expuesto en el código fuente.

**Archivo afectado:** `services/auth-service/src/main/resources/application.yml` (línea 60)

**Código incorrecto:**
```yaml
jwt:
  secret: "mi-super-clave-ultra-segura-de-64-caracteres-2025-1234567890"
```

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Usar variable de entorno `${JWT_SECRET}`.
  - Usar un servicio de secretos externo (HashiCorp Vault, AWS Secrets Manager).
- **Selección:** Se cambió a `${JWT_SECRET}` con un valor por defecto seguro para desarrollo. Es simple, funciona con Docker y perfiles de Spring Boot, y no requiere infraestructura adicional.

## 3. Restricciones de Arquitectura
- Se respeta el patrón de externalización de configuración de Spring Boot.
- Compatible con perfiles `dev` y `docker`.
- El secreto se inyecta mediante variables de entorno en producción.

## 4. Plan de Implementación y Testeo
- [x] Reemplazar secret hardcodeado con `${JWT_SECRET}`
- [x] Agregar en docker-compose.yml la variable de entorno JWT_SECRET
- [x] Verificar que auth-service arranca correctamente
- [x] Test: login y register funcionan con el nuevo secreto

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los secretos nunca deben estar en código fuente, ni siquiera en desarrollo.
- Externalizar secretos es un requisito básico de seguridad.
- Esta corrección es fundamental para la seguridad de todo el sistema de autenticación.
