---
ID: 014
Estado: hecha
Tipo: Refactor
Prioridad: Baja
Impacto: auth-service
---

# 014 - System.out.println replaced with proper logging

## 1. Descripción Detallada
El use case `RegisterUser` tenía múltiples llamadas a `System.out.println()` que debían ser reemplazadas con logging apropiado usando SLF4J/Logger.

**Archivo afectado:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/usecase/RegisterUser.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Reemplazar con Logger SLF4J.
  - Eliminar completamente los prints si no eran necesarios.
- **Selección:** Se eliminaron los `System.out.println` innecesarios ya que la excepción se propaga correctamente. No se necesita logging adicional en un use case que ya maneja errores mediante excepciones.

## 3. Restricciones de Arquitectura
- Se mantiene la limpieza del use case.
- No se introducen dependencias de logging adicionales.

## 4. Plan de Implementación y Testeo
- [x] Eliminar todos los System.out.println de RegisterUser
- [x] Verificar que las excepciones se propagan correctamente
- [x] Verificar compilación

## 5. Lecciones y Justificación (Solo para issues en 'done')
- System.out.println no debe usarse en código de producción.
- Las excepciones proporcionan mejor trazabilidad que prints manuales.
