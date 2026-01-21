# Refactorización de Configuración Neo4j - SocialUser Service

Se ha realizado una reestructuración de la configuración de base de datos Neo4j para el servicio `socialuser-service` con el objetivo de mejorar la seguridad y facilitar el cambio entre entornos de desarrollo (AuraDB Cloud) y producción/local (Docker).

## 1. Estructura de Archivos de Configuración

Se han definido tres perfiles/archivos de configuración en `services/socialuser-service/src/main/resources/`:

| Archivo | Propósito | Estado en Git |
| :--- | :--- | :--- |
| `application.yml` | Configuración base y por defecto para Docker (`bolt://socialgraph-db:7687`). | **Sincronizado** |
| `application-docker.yml` | Configuración específica para el perfil `docker` (usado por `docker-compose`). | **Sincronizado** |
| `application-dev.yml` | Contiene las credenciales secretas de **Neo4j AuraDB Cloud**. | **Ignorado (.gitignore)** |

## 2. Seguridad de Credenciales

Para proteger el acceso a la instancia de la nube, el archivo `application-dev.yml` ha sido añadido al `.gitignore` del servicio. Esto permite que cada desarrollador mantenga sus credenciales de AuraDB localmente sin riesgo de que se filtren en el repositorio de GitHub.

## 3. Comandos de Ejecución

Debido a la estructura multi-módulo del proyecto, es importante ejecutar los comandos apuntando al archivo POM correcto y activando el perfil deseado.

### Ejecución en Desarrollo (Conexión a AuraDB)
Para ejecutar el servicio localmente conectándose a la nube de AuraDB:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev -f services/socialuser-service/pom.xml
```

### Ejecución con Docker
Cuando se utiliza `docker-compose up`, el sistema activa automáticamente el perfil `docker`, por lo que no se requiere ninguna acción manual.

## 4. Mejoras en el `pom.xml`

Se realizaron las siguientes correcciones técnicas para asegurar la estabilidad del build:
- **Eliminación de redundancias**: Se quitó una declaración duplicada de `spring-boot-starter-test`.
- **Estabilización de dependencias**: Se cambió la versión de `testng` de `RELEASE` (obsoleta) a la versión fija `7.10.2`.
- **Corrección de Advertencias**: Se resolvieron los warnings de Maven que afectaban la construcción del modelo efectivo.

---
**Fecha:** 21 de enero de 2026
**Asistente:** Antigravity AI
