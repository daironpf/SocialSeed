---
ID: 027
Estado: hecha
Tipo: Bug
Prioridad: Media
Impacto: auth-service
---

# 027 - Hardcoded gRPC static prefix in application-docker.yml

## 1. Descripción Detallada
La configuración gRPC en `application-docker.yml` tenía un prefijo `static://` incorrecto para Spring gRPC. Esto causaba problemas de conexión entre servicios.

**Archivo afectado:** `services/auth-service/src/main/resources/application-docker.yml`

## 2. Análisis de Soluciones (Trade-offs)
- **Alternativas:**
  - Eliminar el prefijo `static://` incorrecto.
  - Usar la configuración correcta de Spring gRPC.
- **Selección:** Se corrigió la dirección gRPC eliminando el prefijo incorrecto. Sigue la documentación oficial de Spring gRPC.

## 3. Restricciones de Arquitectura
- Se mantiene en application-docker.yml.
- Compatible con el perfil docker.

## 4. Plan de Implementación y Testeo
- [x] Corregir dirección gRPC en application-docker.yml
- [x] Verificar que auth-service se conecta a socialuser-service vía gRPC
- [x] Test: verificar llamada gRPC exitosa

## 5. Lecciones y Justificación (Solo para issues en 'done')
- Los prefijos incorrectos en configuración gRPC causan fallos silenciosos.
- Esta corrección restaura la comunicación gRPC entre servicios.
