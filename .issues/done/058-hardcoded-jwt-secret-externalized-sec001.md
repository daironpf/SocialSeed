---
ID: 058
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: auth-service, platform
---

# 058 - Hardcoded JWT secret externalized (SEC-001)

## 1. Descripción Detallada
El secreto JWT estaba hardcodeado en el código fuente, representando un riesgo de seguridad crítico. Este issue de seguridad fue resuelto externalizando el secreto a una variable de entorno.

**Archivo afectado:** `services/auth-service/src/main/resources/application.yml`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Usar variable de entorno `${JWT_SECRET}`.
  - Usar un servicio de secretos externo.
- **Selección:** Se usó `${JWT_SECRET}` con valor por defecto seguro para desarrollo. Simple y efectivo.

## 3. Restricciones de Arquitectura
- Se respeta el patrón de externalización de Spring Boot.
- Compatible con perfiles dev y docker.

## 4. Plan de Implementación y Testeo
- [x] Reemplazar secret hardcodeado con ${JWT_SECRET}
- [x] Configurar variable de entorno en docker-compose
- [x] Verificar que auth-service arranca correctamente
- [x] Test: login y register funcionan

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los secretos nunca deben estar en código fuente.
- Esta corrección es fundamental para la seguridad del sistema.
