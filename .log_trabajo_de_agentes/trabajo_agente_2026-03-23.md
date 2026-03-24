# Log de Trabajo de Agente - SocialSeed
## Fecha: 23 de Marzo 2026

---

## Objetivo Inicial
1. Leer y entender la estructura del folder `.agent` y sus contenidos
2. Iniciar la infraestructura/servicios con `docker compose`
3. Probar el endpoint `/auth/register` de auth-service

---

## Descubrimientos Iniciales

### Estructura del Proyecto
- **SocialSeed** es un monorepo basado en microservicios usando Spring Boot 3.x y Java 21
- **Microservicios**: auth-service, socialuser-service
- **Infraestructura**: Neo4j, PostgreSQL, Redis, Kafka
- **Arquitectura**: Hexagonal (Domain -> Application -> Infrastructure -> Entry)

### Marco de Agente
- Usa la metodología **Chief Architect** (SPAR-CoT) para resolución de issues
- **10 Skills definidas**: dependency-checker, grpc-manager, infra-guardian, issue-resolver, java-optimization, log-analyzer, neo4j-commander, service-generator, service-starter, Testing Rules
- **4 subdirectorios**: chief_architect, rules, skills, templates

### Problemas de Infraestructura Identificados
- **Neo4j**: El contenedor fallaba por error "Invalid memory configuration - exceeds physical memory"
- **Kafka**: Problemas de inicialización con el script de Topics

---

## Trabajo Realizado

### 1. Configuración de Docker Compose
- Se comentaron los servicios `socialuser-service` y `auth-service` en docker-compose.yml para ejecutar solo infraestructura
- Se ajustaron los parámetros de memoria de Neo4j:
  - `NEO4J_dbms_memory_heap_initial__size=100M`
  - `NEO4J_dbms_memory_heap_max__size=100M`
  - `NEO4J_dbms_memory_pagecache_size=50M`

### 2. Corrección de Conectividad

#### socialuser-service (`application-docker.yml`)
```yaml
# Original (contenedor)
spring:
  neo4j:
    uri: bolt://socialgraph-db:7687
  kafka:
    bootstrap-servers: kafka:9092

# Modificado (localhost)
spring:
  neo4j:
    uri: bolt://localhost:7687
  kafka:
    bootstrap-servers: localhost:9092
```

#### auth-service (`application-docker.yml`)
```yaml
# Original (contenedor)
spring:
  datasource:
    url: jdbc:postgresql://auth-db:5432/authdb
  data:
    redis:
      host: redis
      port: 6379
  grpc:
    client:
      channels:
        user:
          address: socialuser-service:9090
  kafka:
    bootstrap-servers: kafka:9092

# Modificado (localhost)
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/authdb
  data:
    redis:
      host: localhost
      port: 6380
  grpc:
    client:
      channels:
        user:
          address: localhost:9090
  kafka:
    bootstrap-servers: localhost:9092
```

### 3. Solución de Problemas de Kafka

#### Problema 1: Kafka no iniciaba
- El script `topics-init.sh` tenía problemas de rutas en Windows (Git Bash path conversion)
- El contenedor quedaba en un loop de "Waiting for Kafka broker to be ready..."

#### Solución
- Se probaron múltiples aproximaciones:
  1. Correr kafka sin el script de inicialización
  2. Usar configuración KRaft en lugar de ZooKeeper
  3. Limpiar datos de kafka y reiniciar
- Finalmente el contenedor inició correctamente después de varias intentos

### 4. Inicio de Servicios

#### socialuser-service
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=docker -f services/socialuser-service/pom.xml
```
- Puerto: 8090
- gRPC: 9090
- Estado: **UP** (health check)

#### auth-service
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=docker -f services/auth-service/pom.xml
```
- Puerto: 8085
- Estado: **RUNNING**

---

## Resultados de Pruebas

### Registro de Usuario Exitoso

**Request:**
```bash
curl -X POST http://localhost:8085/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"newuser@example.com","username":"newuser","password":"Test1234!@#$"}'
```

**Response:**
```json
{
  "status": 200,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "045fef29-7b43-4aa5-808b-5a3356c70d22",
    "roles": ["ROLE_USER"]
  },
  "message": "Registro realizado correctamente",
  "version": "v0.0.1",
  "timestamp": "2026-03-23T22:02:50.547147200Z"
}
```

### Verificación en Bases de Datos

#### PostgreSQL (auth-db)
```sql
SELECT id, email, username, enabled, email_verified FROM auth_users;
-- Resultado: Usuario creado correctamente
```

#### Neo4j (socialgraph-db)
```cypher
MATCH (u:SocialUser {username: 'newuser'}) RETURN u
-- Resultado: (:SocialUser {language: "EN", id: "f3268ece-...", email: "newuser@example.com", username: "newuser", status: "ACTIVE"})
```

---

## Estado Final de Contenedores

| Contenedor    | Estado           | Puertos                           |
|---------------|------------------|----------------------------------|
| kafka         | Up               | 9092:9092                        |
| socialgraph-db| Up (healthy)     | 7474:7474, 7687:7687            |
| auth-db       | Up (healthy)     | 5433:5432                        |
| redis         | Up (healthy)     | 6380:6379                        |

---

## Archivos Modificados

1. `docker-compose.yml` - Comentado servicios de aplicación, ajustada memoria Neo4j
2. `services/socialuser-service/src/main/resources/application-docker.yml` - Cambiado a localhost
3. `services/auth-service/src/main/resources/application-docker.yml` - Cambiado a localhost

---

## Lecciones Aprendidas

1. **Puerto de Docker en Windows**: En Windows con Docker Desktop, los puertos expuestos están disponibles en `localhost`
2. **Neo4j Memory**: Contenedores Neo4j necesitan suficiente memoria asignada; reducir heap a 100M resuelve problemas en sistemas con RAM limitada
3. **Kafka en Docker Compose**: El script de inicialización de topics puede tener problemas de rutas en diferentes sistemas operativos

---

## Próximos Pasos Sugeridos

1. Crear topics de Kafka manualmente si son necesarios
2. Configurar el perfil `dev` para usar AuraDB (Neo4j Cloud) en lugar del contenedor local
3. Integrar más endpoints (login, follow, etc.)
4. Configurar el API Gateway
