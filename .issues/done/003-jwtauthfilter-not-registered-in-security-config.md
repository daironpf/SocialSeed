---
ID: 003
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: auth-service
---

# 003 - JwtAuthFilter not registered in SecurityConfig

## 1. Descripción Detallada
El `JwtAuthFilter` no estaba registrado en el filter chain de `SecurityConfig`. La clase no tenía la anotación `@Component` y no se añadía al chain de filtros de Spring Security. Esto significaba que la autenticación JWT no se aplicaba a los endpoints protegidos, dejando el sistema vulnerable.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/config/security/JwtAuthFilter.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir `@Component` al filtro y usar `addFilterBefore` en SecurityConfig.
  - Registrar el filtro como bean en una clase de configuración dedicada.
- **Selección:** Se añadió `@Component` y se registró con `addFilterBefore` en SecurityConfig. Es el enfoque estándar de Spring Security y mantiene la configuración centralizada.

## 3. Restricciones de Arquitectura
- El filtro permanece en `config.security`, su ubicación correcta.
- Se respeta el orden de filtros de Spring Security.
- No se modifica la lógica interna del filtro.

## 4. Plan de Implementación y Testeo
- [x] Añadir `@Component` a JwtAuthFilter
- [x] Registrar con `addFilterBefore` en SecurityConfig
- [x] Verificar que endpoints protegidos rechazan requests sin token
- [x] Verificar que endpoints protegidos aceptan requests con token válido

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Un filtro de seguridad no registrado equivale a no tener seguridad.
- Es crítico verificar que todos los componentes de seguridad están activos en el filter chain.
- Esta corrección es fundamental para la integridad de todo el sistema de autenticación.
