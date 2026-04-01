---
ID: 042
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: infrastructure, docker-compose
---

# 042 - Windows-specific volume paths changed to relative

## 1. Descripción Detallada
El `docker-compose.yml` tenía rutas de volumen específicas de Windows (`D:/db_volumes/...`) que no funcionaban en otros sistemas operativos y eran difíciles de mantener.

**Archivo afectado:** `docker-compose.yml`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Cambiar a rutas relativas `./infrastructure/volumes/`.
  - Usar named volumes de Docker.
- **Selección:** Se cambiaron a rutas relativas `./infrastructure/volumes/`. Es portable entre sistemas operativos y mantiene los datos organizados en el repositorio.

## 3. Restricciones de Arquitectura
- Se mantiene en docker-compose.yml.
- Las rutas relativas son portables.

## 4. Plan de Implementación y Testeo
- [x] Cambiar rutas absolutas de Windows a rutas relativas
- [x] Crear directorio infrastructure/volumes si no existe
- [x] Verificar que docker-compose up funciona correctamente
- [x] Test: verificar que datos persisten correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Las rutas absolutas específicas de OS rompen la portabilidad.
- Esta corrección hace que docker-compose funcione en cualquier sistema.
