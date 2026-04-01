---
ID: 016
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: socialuser-service
---

# 016 - ChangeUsername/ChangeEmail bypass domain model

## 1. Descripción Detallada
Los use cases `ChangeUsername` y `ChangeEmail` en socialuser-service accedían directamente al repositorio en lugar de usar los métodos del modelo de dominio `User.changeUsername()` y `User.changeEmail()`. Esto violaba la arquitectura hexagonal al bypassar la lógica de negocio del dominio.

**Archivos afectados:**
- `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/application/usecase/ChangeUsername.java`
- `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/application/usecase/ChangeEmail.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Refactorizar para usar métodos del dominio `User.changeUsername/Email`.
  - Mantener acceso directo al repositorio pero añadir validaciones.
- **Selección:** Se refactorizaron ambos use cases para usar los métodos del dominio. Respeta la arquitectura hexagonal y centraliza la lógica de negocio en la entidad.

## 3. Restricciones de Arquitectura
- Se respeta la arquitectura hexagonal: el dominio encapsula la lógica de negocio.
- Los use cases orquestan, no implementan lógica de negocio.
- Se mantiene `@Transactional` en los use cases.

## 4. Plan de Implementación y Testeo
- [x] Refactorizar ChangeUsername para usar User.changeUsername()
- [x] Refactorizar ChangeEmail para usar User.changeEmail()
- [x] Verificar compilación
- [x] Test unitario: verificar que la lógica de dominio se ejecuta

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los use cases deben orquestar, no implementar lógica de negocio.
- El dominio es el núcleo de la arquitectura hexagonal y debe encapsular las reglas de negocio.
- Esta corrección fortalece la separación de responsabilidades.
