---
ID: 010
Estado: hecha
Tipo: Bug
Prioridad: Alta
Impacto: infrastructure, api-gateway
---

# 010 - api-gateway route port mismatch

## 1. Descripción Detallada
El api-gateway tenía configurado el puerto incorrecto para enrutar a socialuser-service: usaba puerto 4000 en lugar del 8090 correcto. Esto hacía imposible que el gateway alcanzara el servicio.

**Archivo afectado:** `services/api-gateway/src/main/resources/application.yml`

**Configuración incorrecta:**
```yaml
routes:
  - id: socialuser-route
    uri: http://socialuser-service:4000  # WRONG - should be 8090
```

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Corregir el puerto en la configuración del gateway.
  - Usar service discovery (Eureka) para resolución dinámica.
- **Selección:** Se corrigió el puerto a 8090. Es el fix inmediato y correcto. Service discovery sería overkill para el estado actual del proyecto.

## 3. Restricciones de Arquitectura
- Se mantiene la configuración estática de rutas del gateway.
- El puerto 8090 es el estándar de socialuser-service.
- Compatible con perfiles dev y docker.

## 4. Plan de Implementación y Testeo
- [x] Cambiar puerto de 4000 a 8090 en application.yml del api-gateway
- [x] Verificar que el gateway puede enrutar a socialuser-service
- [x] Test manual: curl a través del gateway a socialuser-service

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los puertos incorrectos en configuración son errores silenciosos difíciles de debuggear.
- Mantener consistencia de puertos entre servicios es crítico.
- Esta corrección restaura la conectividad del sistema.
