---
ID: 037
Estado: hecha
Tipo: Feature
Prioridad: Media
Impacto: platform, socialseed-error-handling-starter
---

# 037 - Missing KafkaExceptionHandler created

## 1. Descripción Detallada
El starter `socialseed-error-handling-starter` no tenía un manejador de excepciones para errores de Kafka (`KafkaException`, `ProducerException`, `ConsumerException`). Los errores de Kafka no se traducían en respuestas API estándar.

**Archivo creado:** `platform/socialseed-error-handling-starter/src/main/java/com/socialseed/errorhandling/exceptionhandler/KafkaExceptionHandler.java`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Crear handler especializado para Kafka.
  - Manejar errores de Kafka en el GlobalErrorHandler.
- **Selección:** Se creó `KafkaExceptionHandler` con `@RestControllerAdvice`. Mantiene el GlobalErrorHandler limpio y proporciona manejo específico para errores de mensajería.

## 3. Restricciones de Arquitectura
- Se mantiene en `platform/socialseed-error-handling-starter`.
- Usa `@RestControllerAdvice` para tipos específicos de excepción Kafka.

## 4. Plan de Implementación y Testeo
- [x] Crear KafkaExceptionHandler con @RestControllerAdvice
- [x] Manejar KafkaException, ProducerException, ConsumerException
- [x] Verificar que Spring escanea el handler
- [x] Test de integración: verificar que errores de Kafka se manejan correctamente

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los handlers especializados mantienen el código limpio y proporcionan mensajes específicos.
- Esta corrección mejora la observabilidad de errores de mensajería.
