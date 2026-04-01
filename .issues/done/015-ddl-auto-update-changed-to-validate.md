---
ID: 015
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: auth-service
---

# 015 - DDL auto update changed to validate

## 1. Descripción Detallada
El auth-service tenía `ddl-auto: update` en la configuración de Hibernate JPA. Esto permite que Hibernate modifique el esquema de base de datos automáticamente, lo cual es peligroso en producción ya puede causar pérdida de datos o cambios no controlados.

**Archivo afectado:** `services/auth-service/src/main/resources/application.yml` (línea 27)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Cambiar a `validate` para solo verificar que el esquema coincide con las entidades.
  - Usar Flyway/Liquibase para migraciones controladas.
- **Selección:** Se cambió a `validate`. Es el cambio inmediato más seguro. Flyway/Liquibase se implementará cuando el proyecto escale.

## 3. Restricciones de Arquitectura
- Se mantiene la configuración JPA de Hibernate.
- Compatible con perfiles dev y docker.
- Los DDL deben estar documentados externamente.

## 4. Plan de Implementación y Testeo
- [x] Cambiar `ddl-auto: update` a `ddl-auto: validate`
- [x] Verificar que el esquema existente coincide con las entidades
- [x] Verificar que auth-service arranca correctamente
- [x] Test: registro y login funcionan sin cambios de esquema

## 5. Lecciones y Justificación (Solo para issues en 'done')
- `ddl-auto: update` es peligroso en producción.
- `validate` asegura integridad del esquema sin modificaciones automáticas.
- Esta corrección previene cambios no controlados en la base de datos.
