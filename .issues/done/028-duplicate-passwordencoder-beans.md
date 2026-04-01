---
ID: 028
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: auth-service
---

# 028 - Duplicate PasswordEncoder beans

## 1. Descripción Detallada
Tanto `UserSecurityConfig` como la auto-configuración de Spring Security creaban beans de `PasswordEncoder`, causando un conflicto potencial. Esto podía causar comportamiento impredecible en la codificación de contraseñas.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/config/security/UserSecurityConfig.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir `@Primary` al bean de PasswordEncoder en UserSecurityConfig.
  - Eliminar el bean duplicado y usar solo la auto-configuración.
- **Selección:** Se añadió `@Primary` al bean de UserSecurityConfig. Mantiene la configuración explícita y resuelve la ambigüedad.

## 3. Restricciones de Arquitectura
- Se mantiene en `config.security`.
- No se modifica la lógica de codificación.

## 4. Plan de Implementación y Testeo
- [x] Añadir @Primary al bean PasswordEncoder en UserSecurityConfig
- [x] Verificar compilación
- [x] Test: verificar que registro y login funcionan correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los beans duplicados causan ambigüedad y comportamiento impredecible.
- @Primary resuelve conflictos de beans de forma explícita.
- Esta corrección asegura que se usa el PasswordEncoder correcto.
