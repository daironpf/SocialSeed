---
ID: 005
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: auth-service
---

# 005 - RegisterUser empty catch block

## 1. Descripción Detallada
El use case `RegisterUser` tenía un catch block vacío que capturaba todas las excepciones silenciosamente y continuaba con un UUID aleatorio. Esto podía crear un estado inconsistente donde el registro parecía exitoso pero fallaba internamente.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/usecase/RegisterUser.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Eliminar el try-catch y dejar que la excepción se propague.
  - Capturar y relanzar como `BusinessException` con mensaje claro.
- **Selección:** Se eliminó el `System.out.println` y se relanza como `BusinessException`. Mantiene el control de errores sin silenciar fallos críticos.

## 3. Restricciones de Arquitectura
- Se respeta la arquitectura hexagonal: el use case propaga errores correctamente.
- Se usa `BusinessException` del error-handling starter.
- Se elimina código de debug (`System.out.println`).

## 4. Plan de Implementación y Testeo
- [x] Eliminar `System.out.println` del catch block
- [x] Relanzar excepción como `BusinessException`
- [x] Verificar que el registro falla correctamente con datos inválidos
- [x] Test unitario: verificar que excepciones se propagan correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Catch blocks vacíos son uno de los bugs más peligrosos: silencian fallos sin rastro.
- Siempre propagar o loguear excepciones, nunca silenciarlas.
- Esta corrección previene registros corruptos o inconsistentes.
