---
ID: 082
Estado: pendiente
Tipo: Bug
Prioridad: Baja
Impacto: testing
---

# 082 - conftest.py type error

## 1. Descripción Detallada
El archivo `testing/tests/conftest.py` tiene un error de tipo en las líneas 15 y 19. Pyright reporta: `Return type of generator function must be compatible with "Generator[AuthPage, Any, Any]"`.

**Archivo afectado:** `testing/tests/conftest.py` (líneas 15, 19)

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Cambiar el type hint para que sea compatible con Generator.
  - Usar sintaxis correcta de pytest generator.
- **Selección:** Corregir el type hint o usar sintaxis correcta de pytest generator. Elimina el error de tipo sin cambiar la funcionalidad.

## 3. Restricciones de Arquitectura
- Tests en `testing/tests/`.
- Mantener compatibilidad con pytest.
- Seguir testing-rules.md.

## 4. Plan de Implementación y Testeo
- [ ] Revisar error de tipo en conftest.py
- [ ] Corregir type hint o sintaxis de generator
- [ ] Ejecutar pyright/type checker y verificar que error desaparece
- [ ] Ejecutar tests y verificar que pasan

## 5. Lecciones y Justificación (Solo para issues en 'done')
- [Pendiente]
