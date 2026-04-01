---
ID: 040
Estado: hecha
Tipo: Refactor
Prioridad: Media
Impacto: infrastructure, docker-compose
---

# 040 - Kafka topics initialized in Docker

## 1. Descripción Detallada
El script `infrastructure/kafka/topics-init.sh` existía pero no se montaba ni ejecutaba en el contenedor de Kafka en `docker-compose.yml`. Los tópicos no se creaban automáticamente al iniciar Kafka.

**Archivo afectado:** `docker-compose.yml`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Montar el script como volume y ejecutarlo en el contenedor de Kafka.
  - Crear tópicos manualmente después de iniciar Kafka.
- **Selección:** Se montó el script como volumen en el contenedor de Kafka. Automatiza la creación de tópicos al inicio.

## 3. Restricciones de Arquitectura
- Se mantiene en docker-compose.yml.
- El script debe ser compatible con la versión de Kafka usada.

## 4. Plan de Implementación y Testeo
- [x] Montar topics-init.sh como volumen en contenedor Kafka
- [x] Configurar ejecución automática del script
- [x] Verificar que tópicos se crean al iniciar Kafka
- [x] Test: verificar que producers y consumers pueden usar los tópicos

## 5. Lecciones y Justificación (Solo para issues en 'done')
- La inicialización automática de tópicos es esencial para el desarrollo.
- Esta corrección elimina la necesidad de crear tópicos manualmente.
