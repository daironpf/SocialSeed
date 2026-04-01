---
ID: 046
Estado: hecha
Tipo: Refactor
Prioridad: Baja
Impacto: auth-service
---

# 046 - Logout returns proper ApiResponse body

## 1. Descripción Detallada
El endpoint de logout retornaba un cuerpo vacío (204) pero el manejo de respuesta era inconsistente con el formato estándar de ApiResponse.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/entry/rest/controller/LogoutController.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Retornar ApiResponse.success() con mensaje de logout exitoso.
  - Mantener 204 sin cuerpo.
- **Selección:** Se cambió a retornar `ApiResponse.success()` con mensaje de logout exitoso. Mantiene consistencia con el formato de respuestas del proyecto.

## 3. Restricciones de Arquitectura
- Se mantiene en `entry.rest.controller`.
- Se usa ApiResponse del api-response-starter.

## 4. Plan de Implementación y Testeo
- [x] Cambiar retorno a ApiResponse.success() con mensaje
- [x] Verificar compilación
- [x] Test: verificar que logout retorna respuesta consistente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Las respuestas consistentes mejoran la experiencia del cliente.
- Esta corrección alinea logout con el estándar de ApiResponse.
