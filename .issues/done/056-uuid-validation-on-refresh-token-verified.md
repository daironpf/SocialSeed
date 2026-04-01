---
ID: 056
Estado: hecha
Tipo: Bug
Prioridad: Baja
Impacto: auth-service
---

# 056 - UUID validation on refresh token verified as correct

## 1. Descripción Detallada
Se reportó que el `RefreshTokenRequestDTO` validaba tokens como UUID estándar, lo cual podía causar fallos de validación ya que los refresh tokens no son UUID estándar. Tras análisis, se determinó que los tokens SÍ son UUID estándar generados por `UUID.randomUUID()`, por lo que la validación es correcta.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/entry/rest/dto/request/RefreshTokenRequestDTO.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Cambiar validación de UUID a String genérico.
  - Mantener validación UUID ya que los tokens son UUID válidos.
- **Selección:** Se mantuvo la validación UUID. Los refresh tokens se generan con `UUID.randomUUID()` y son UUID válidos.

## 3. Restricciones de Arquitectura
- No se requieren cambios.
- La validación existente es correcta.

## 4. Plan de Implementación y Testeo
- [x] Verificar que refresh tokens son UUID estándar
- [x] Confirmar que validación funciona correctamente
- [x] Test: verificar que refresh token funciona con UUID válido

## 5. Lecciones y Justificación (Solo para issues en 'done')
- No todos los reportes de bugs son bugs reales.
- El análisis cuidadoso previene cambios innecesarios.
- Esta verificación confirma que la validación es correcta.
