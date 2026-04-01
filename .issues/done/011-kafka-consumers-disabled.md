---
ID: 011
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: socialuser-service, infrastructure
---

# 011 - Kafka consumers disabled

## 1. Descripción Detallada
Los consumidores de Kafka en socialuser-service estaban deshabilitados con `auto-startup: false` en la configuración. Esto significaba que el servicio no consumía ningún evento de Kafka, rompiendo la comunicación asíncrona con auth-service y otros servicios.

**Archivo afectado:** `services/socialuser-service/src/main/resources/application.yml`

**Configuración incorrecta:**
```yaml
kafka:
  listener:
    auto-startup: false  # CONSUMERS ARE DISABLED!
```

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Cambiar a `auto-startup: true` en application.yml.
  - Controlar el arranque programáticamente con `KafkaListenerEndpointRegistry`.
- **Selección:** Se cambió a `auto-startup: true`. Es la configuración correcta para producción y permite que los consumidores arranquen automáticamente con el servicio.

## 3. Restricciones de Arquitectura
- Se mantiene la configuración de Kafka en application.yml.
- Compatible con perfiles dev y docker.
- Los topics deben estar versionados (.v1) según estándares del proyecto.

## 4. Plan de Implementación y Testeo
- [x] Cambiar `auto-startup: false` a `auto-startup: true`
- [x] Verificar que los consumidores arrancan con el servicio
- [x] Test de integración: enviar evento a Kafka y verificar que se consume
- [x] Verificar logs de arranque del KafkaListener

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los consumidores deshabilitados son un bug silencioso que solo se detecta cuando los eventos no llegan.
- `auto-startup: true` es la configuración esperada en producción.
- Esta corrección habilita la comunicación asíncrona entre microservicios.
