# Issue: Configurar Perfiles de Ejecución para Servicios

## ID
pending-docker-native-profiles

## Estado
RESOLVED (Completado)

## Prioridad
HIGH

---

## Descripción del Problema

Los servicios `auth-service` y `socialuser-service` necesitaban tener configuraciones separadas para:
1. Ejecutarse como contenedores Docker (dentro de la red de docker-compose)
2. Ejecutarse nativamente con Maven conectados a contenedores de infraestructura

---

## Solución Implementada

### Cambios en docker-compose.yml

1. **Configuración de variables de entorno** para auth-service:
   - `SPRING_PROFILES_ACTIVE: docker`
   - `SPRING_DATASOURCE_URL: jdbc:postgresql://auth-db:5432/authdb`
   - `SPRING_DATASOURCE_USERNAME/PASSWORD`
   - `SPRING_DATA_REDIS_HOST/PORT/PASSWORD`
   - `SPRING_GRPC_CLIENT_CHANNELS_USER_ADDRESS: socialuser-service:9090`

2. **Configuración de variables de entorno** para socialuser-service:
   - `SPRING_PROFILES_ACTIVE: docker`
   - `SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092`
   - `SPRING_NEO4J_URI: bolt://socialgraph-db:7687`

3. **Volúmenes removidos** - Ya no se montan archivos de configuración externos para evitar conflictos de prioridad

### Cambios en Configuración de Servicios

- Los servicios ahora usan variables de entorno para la configuración en Docker
- Las configuraciones de perfil `docker` se mantienen en los archivos `application-docker.yml`
- Las variables de entorno tienen precedencia sobre los archivos de configuración

---

## Resultado Final

### Contenedores Activos
| Servicio | Estado | Puerto |
|----------|--------|--------|
| auth-service | healthy | 8085 |
| socialuser-service | healthy | 8090, 9090 (gRPC) |
| auth-db (PostgreSQL) | healthy | 5433 |
| redis | healthy | 6380 |
| socialgraph-db (Neo4j) | healthy | 7474, 7687 |
| kafka | running | 9092 |

### Verificación Exitosa

1. **Registro**: `POST /auth/register` - ✅ Funciona
   ```bash
   curl -X POST http://localhost:8085/auth/register \
     -H "Content-Type: application/json" \
     -d '{"email":"dockertest@example.com","username":"dockertest","password":"Test1234!@#$"}'
   ```

2. **Login**: `POST /auth/login` - ✅ Funciona
   ```bash
   curl -X POST http://localhost:8085/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"dockertest@example.com","password":"Test1234!@#$"}'
   ```

3. **PostgreSQL**: Usuario creado correctamente - ✅
   ```sql
   SELECT email, username FROM auth_users;
   ```

4. **Neo4j**: SocialUser creado correctamente - ✅
   ```cypher
   MATCH (u:SocialUser) RETURN u.username, u.email;
   ```

---

## Configuración de Red Docker

Todos los servicios están en la red `socialseed_net` y se comunican usando:
- Nombres de contenedores como hostnames
- Puerto interno de cada servicio

### Endpoints Disponibles
- **auth-service**: `http://localhost:8085`
  - `POST /auth/register`
  - `POST /auth/login`
  - `GET /actuator/health`
- **socialuser-service**: `http://localhost:8090`
  - `GET /actuator/health`
  - gRPC: `localhost:9090`

---

## Notas

1. Los volúmenes fueron removidos del docker-compose.yml para evitar conflictos de configuración
2. Las variables de entorno SPRING_* son necesarias para la configuración en Docker
3. El perfil `docker` se activa automáticamente con `SPRING_PROFILES_ACTIVE: docker`
4. Kafka puede mostrar "unhealthy" pero esto no afecta el flujo principal de registro/login
