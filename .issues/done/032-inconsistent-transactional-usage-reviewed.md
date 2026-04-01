---
ID: 032
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: socialuser-service
---

# 032 - Inconsistent @Transactional usage reviewed

## 1. Descripción Detallada
Los use cases de socialuser-service tenían uso inconsistente de `@Transactional`. Algunos lo tenían y otros no, sin un patrón claro. Esto podía causar problemas de atomicidad en operaciones de base de datos.

**Archivos afectados:** Múltiples use cases en `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/application/usecase/`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir @Transactional a todos los use cases que modifican datos.
  - Revisar cada use case individualmente y decidir.
- **Selección:** Se revisó cada use case. Se añadió @Transactional donde era necesario (operaciones de escritura) y se mantuvo sin @Transactional en lecturas puras.

## 3. Restricciones de Arquitectura
- Se mantiene en la capa de aplicación/usecase.
- Se respeta el principio de transaccionalidad mínima necesaria.

## 4. Plan de Implementación y Testeo
- [x] Revisar cada use case para necesidad de @Transactional
- [x] Añadir @Transactional a CreateUser, DeleteUser, UpdateUserProfile, StartVacation, EndVacation, ChangeUsername, ChangeEmail
- [x] Mantener sin @Transactional en GetUserById, GetUserByEmail, GetUserByName, GetAllUsers
- [x] Verificar compilación

## 5. Lecciones y Justificación (Solo para issues en 'done')
- La transaccionalidad debe ser explícita y consistente.
- Las operaciones de lectura no necesitan @Transactional.
- Esta corrección asegura atomicidad en operaciones de escritura.
