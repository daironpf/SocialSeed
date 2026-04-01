---
ID: 017
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: socialuser-service
---

# 017 - CreateUser missing @Transactional

## 1. Descripción Detallada
El use case `CreateUser` era el único use case en socialuser-service sin la anotación `@Transactional`. Esto podía causar inconsistencias si la creación del usuario en PostgreSQL y la creación del SocialUser en Neo4j no se ejecutaban atómicamente.

**Archivo afectado:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/application/usecase/CreateUser.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Añadir `@Transactional` al método execute.
  - Manejar transacciones manualmente con PlatformTransactionManager.
- **Selección:** Se añadió `@Transactional` al use case. Es el enfoque estándar de Spring y garantiza atomicidad.

## 3. Restricciones de Arquitectura
- Se mantiene en la capa de aplicación/usecase.
- La transacción cubre PostgreSQL; Neo4j tiene su propia transacción.

## 4. Plan de Implementación y Testeo
- [x] Añadir @Transactional a CreateUser
- [x] Verificar compilación
- [x] Test: verificar que la creación es atómica

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Todos los use cases que modifican datos deben ser transaccionales.
- Esta corrección previene estados inconsistentes en la base de datos.
