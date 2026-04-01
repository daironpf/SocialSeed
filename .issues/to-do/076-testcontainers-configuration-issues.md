---
ID: 076
Estado: pendiente
Tipo: Bug
Prioridad: Media
Impacto: auth-service, socialuser-service
---

# 076 - Testcontainers configuration issues

## 1. Descripción Detallada
Los tests de integración usan `withoutAuthentication()` pero la producción requiere autenticación. Esto puede causar que los tests pasen pero la producción falle.

**Archivos afectados:**
- `services/auth-service/src/test/java/` (configuración de Testcontainers)
- `services/socialuser-service/src/test/java/` (configuración de Testcontainers)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Configurar Testcontainers con autenticación similar a producción.
  - Mantener sin autenticación pero documentar la diferencia.
- **Selección:** Configurar Testcontainers con autenticación para que los tests reflejen mejor el entorno de producción.

## 3. Restricciones de Arquitectura
- Tests de integración en `src/test/java/`.
- Usar Testcontainers para bases de datos.
- Mantener tests rápidos y confiables.

## 4. Plan de Implementación y Testeo
- [ ] Revisar configuración de Testcontainers en auth-service
- [ ] Revisar configuración de Testcontainers en socialuser-service
- [ ] Añadir autenticación a los contenedores de prueba
- [ ] Ejecutar tests de integración y verificar que pasan

## 5. Lecciones y Justificación (Solo para issues en 'done')
- [Pendiente]
