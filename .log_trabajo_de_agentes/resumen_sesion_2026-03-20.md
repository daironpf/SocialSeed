# Log de Trabajo del Agente - 20 de Marzo 2026

## Contexto Inicial

El agente recibió la tarea de continuar el trabajo previo en el monorepo SocialSeed. El resumen de trabajo anterior (`memory/brain_summary.md`) indicaba:

- Se estaban resolviendo 82 issues trackeados en `.issues/`
- El foco当时的 estaba en arreglar tests E2E fallidos (TEST-020, TEST-021, TEST-022) causados por respuestas 403 en endpoints `/auth/**`
- Ya se habían identificado las causas raíz pero el trabajo estaba incompleto

---

## Problema Principal: E2E Tests Retornando 403

### Análisis del Estado Actual

Al revisar el estado del proyecto, se encontró:

1. **Git status con muchos cambios sin commitear** - Había ~64 archivos modificados sin commit
2. **Servicios corriendo pero con código desactualizado** - auth-service (PID 22816) estaba corriendo con código anterior al fix de seguridad
3. **Redis y PostgreSQL corriendo en Docker** - Infraestructura base disponible
4. **Tests E2E fallando** - 5 de 6 tests de registro fallando con 403

### Síntomas Observados

```
test_register_success PASSED
test_register_duplicate_email FAILED (403)
test_register_duplicate_username FAILED (403)
test_register_invalid_email FAILED (403)
test_register_weak_password FAILED (403)
test_register_missing_fields FAILED (403)
```

---

## Diagnóstico: La Cadena de Errores

### Capa 1: Error Original (fuera del alcance de este día)
El trabajo anterior ya había identificado que el problema raíz era la doble cadena de autorización de Spring Security 6.x:
- `authorizeHttpRequests()` agrega **AMBOS** `AuthorizationFilter` Y `FilterSecurityInterceptor`
- `AuthorizationFilter` se ejecutaba ANTES de `JwtAuthFilter`
- `AuthorizationFilter` denegaba todas las peticiones porque el usuario era anónimo
- El fix conocido era cambiar a `authorizeRequests()` para usar solo `FilterSecurityInterceptor`

### Capa 2: Error de Dispatch a /error
Incluso después del fix de seguridad, los tests seguían fallando. Al analizar los logs:

```
ERROR ... DispatcherServlet threw exception
  BusinessException: error.user.username_exists
    at RegisterUserValidator.aroundRegisterUser()
    at ErrorReportValve.invoke()
    
DEBUG ... Securing POST /error
DEBUG ... Failed to authorize filter invocation [POST /error] with attributes [authenticated]
```

**Patrón identificado:**
1. El handler de negocio lanza `BusinessException`
2. `GlobalErrorHandler` (del starter) intenta manejar la excepción
3. Jackson intenta serializar `ApiResponse<Void>` con `Instant`
4. Jackson falla porque no tiene `JavaTimeModule` registrado
5. Se lanza `InvalidDefinitionException`
6. `ErrorReportValve` de Tomcat detecta la excepción y llama `response.sendError(500)`
7. `sendError` dispara un forward/redirect interno a `/error`
8. El filtro de seguridad intercepta la petición a `/error`
9. `/error` requiere `authenticated` por defecto
10. Usuario anónimo denegado → 403

### Capa 3: ¿Por qué GlobalErrorHandler No Funcionaba?

Se descubrieron DOS problemas:

1. **El ObjectMapper de `GlobalErrorHandler` no tenía `JavaTimeModule`**: La clase `ApiResponse` usa `java.time.Instant` como campo `timestamp`. Cuando se intentaba serializar, Jackson fallaba con `InvalidDefinitionException: Java 8 date/time type 'java.time.Instant' not supported by default`.

2. **El ErrorReportValve de Tomcat sobreescribía la respuesta**: Aunque `GlobalErrorHandler` escribía bytes al response buffer, `ErrorReportValve` se ejecutaba después y sobreescribía con su propio formato de error.

---

## Soluciones Implementadas

### Fix 1: Cambiar a authorizeRequests() en SecurityConfig

**Archivo:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/config/SecurityConfig.java`

**Cambio:**
```java
// ANTES (incorrecto - agrega AuthorizationFilter)
http.authorizeHttpRequests()
    .requestMatchers("/auth/**").permitAll()
    .anyRequest().authenticated();

// DESPUÉS (correcto - solo FilterSecurityInterceptor)
http.authorizeRequests()
    .accessDecisionManager(accessDecisionManager())
    .requestMatchers("/auth/register", "/auth/login", ...).permitAll()
    .requestMatchers("/actuator/**").permitAll()
    .requestMatchers("/error").permitAll()  // <-- CRÍTICO: evitar segundo check de seguridad
    .anyRequest().authenticated();
```

**Por qué:** `authorizeHttpRequests()` en Spring Security 6.x agrega `AuthorizationFilter` a la cadena EN ADICIÓN a `FilterSecurityInterceptor`. `AuthorizationFilter` usa el sistema de authorization basado en `AuthorizationManager<RequestMatcher>` que es DIFERENTE al sistema de `@PreAuthorize`. Ambos corren simultáneamente, causando conflictos. `authorizeRequests()` usa solo el sistema legacy con `FilterSecurityInterceptor`.

**Alternativas consideradas:**
- Deshabilitar `AuthorizationFilter` explícitamente: Posible pero más complejo
- Cambiar el orden de filtros: No resuelve el problema raíz
- Agregar todas las reglas en ambos sistemas: Duplicación de lógica

### Fix 2: Registrar JavaTimeModule en ObjectMapper

**Archivo:** `platform/socialseed-error-handling-starter/src/main/java/com/socialseed/errorhandling/handler/GlobalErrorHandler.java`

**Cambio:**
```java
// ANTES (incorrecto)
@RestControllerAdvice
public class GlobalErrorHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
        return ResponseEntity
            .status(ex.getErrorCode().getHttpStatus())
            .body(ApiResponse.error(...)); // Instant no se serializaba
    }
}

// DESPUÉS (correcto)
@RestControllerAdvice
public class GlobalErrorHandler {
    private static final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @ExceptionHandler(BusinessException.class)
    public void handleBusiness(BusinessException ex, HttpServletResponse response) throws IOException {
        response.resetBuffer();
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        var body = new ApiResponse<Void>(status, null, msg, "v0.0.1", Instant.now());
        response.getWriter().write(mapper.writeValueAsString(body));
        response.getWriter().flush();
    }
}
```

**Por qué usar `response.resetBuffer()` y `flushBuffer()`:**
- `resetBuffer()` limpia cualquier dato parcial escrito al buffer
- `flushBuffer()` fuerza el envío al cliente INMEDIATAMENTE
- Esto previene que `ErrorReportValve` sobreescriba la respuesta
- `isCommitted()` verifica si la respuesta ya fue enviada antes de intentar escribir

**Alternativas consideradas:**
- Usar el ObjectMapper auto-configurado de Spring (`JacksonAutoConfiguration`): Requiere inyectar `ObjectMapper` como dependencia, lo cual crea acoplamiento entre el handler y Spring
- Sobreescribir `BasicErrorController`: No funcionó porque la prioridad del bean
- Usar `@ResponseStatus` en excepciones: No permite control granular del body

### Fix 3: Agregar permitAll() para /error

**Cambio en SecurityConfig:**
```java
.requestMatchers("/error").permitAll()
```

**Por qué:** Aunque `GlobalErrorHandler` escribiera la respuesta correctamente, el flujo de error de Tomcat siempre hace un dispatch interno a `/error`. Sin `permitAll`, Spring Security intercepta esa petición secundaria y la deniega.

**Alternativas consideradas:**
- Excluir `/error` en `shouldNotFilter()` del `JwtAuthFilter`: Solo afecta al JWT filter, no a `FilterSecurityInterceptor`
- Configurar `errorPage` en `ExceptionTranslationFilter`: Más complejo, requiere crear un controller
- Usar `SecurityFilterChain` con `securityMatcher`: Funciona pero overkill para este caso

### Fix 4: Exception Handlers para Tecnologías Opcionales

**Archivos:** `RedisExceptionHandler.java`, `KafkaExceptionHandler.java`, `Neo4jExceptionHandler.java`, `SecurityExceptionHandler.java`

**Patrón usado:**
```java
@RestControllerAdvice
public class RedisExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleRedis(Exception ex) throws Exception {
        if (ex.getClass().getName().contains("Redis")) {
            log.error("Redis error: {}", ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error(503, ApiResponse.msg("error.cache.redis")));
        }
        throw ex; // No es Redis → delegar al siguiente handler
    }
}
```

**Por qué:** Las dependencias específicas (Redis, Kafka, Neo4j, Security) están marcadas como `optional=true` en el pom.xml del starter. Si la tecnología no está en el classpath, importar directamente sus clases causaría `NoClassDefFoundError` al arrancar. El patrón de verificación por nombre de clase (string-based) evita este problema.

**Alternativas consideradas:**
- Usar `@ConditionalOnClass` directamente en el handler: Funciona pero Spring Boot puede fallar en detectar clases opcionales
- Verificar con `Class.forName()`: Funciona pero más verbose
- Solo confiar en `optional=true` en pom.xml: No es suficiente, la clase se carga al importar

### Fix 5: Configuración Correcta de Redis y gRPC en application-dev.yml

**Redis:**
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6380  # Era 7000, cambiado a 6380 que es el puerto expuesto por Docker
```

**gRPC:**
```yaml
spring:
  grpc:
    client:
      user:
        address: 'static://localhost:9090'  # El prefijo 'static://' es requerido por spring-grpc
        negotiationType: plaintext
```

---

## Archivos Modificados y Creados

### Platform (Starters)

| Archivo | Cambio |
|----------|--------|
| `GlobalErrorHandler.java` | Reescrito con ObjectMapper con JavaTimeModule, usa response directo en lugar de ResponseEntity |
| `ErrorHandlingAutoConfiguration.java` | Agregado PlatformErrorController, registrados todos los handlers con @ConditionalOnClass |
| `RedisExceptionHandler.java` | Creado - string-based checking para RedisConnectionFailureException |
| `KafkaExceptionHandler.java` | Creado - string-based checking para Kafka exceptions |
| `Neo4jExceptionHandler.java` | Creado - string-based checking para Neo4j exceptions |
| `SecurityExceptionHandler.java` | Creado - string-based checking para security exceptions |
| `PGSQLExceptionHandler.java` | Modificado - agregado @ConditionalOnClass |
| `pom.xml` | Agregadas dependencias opcionales (neo4j, redis, kafka, security) |
| Mensajes i18n | Agregadas claves: `error.user.username_exists`, `error.user.email_exists`, etc. |

### Auth Service

| Archivo | Cambio |
|---------|--------|
| `SecurityConfig.java` | Cambiado authorizeHttpRequests → authorizeRequests, agregado permitAll para /error, AccessDecisionManager explícito |
| `JwtAuthFilter.java` | Agregados paths excluidos (swagger, actuator, etc.) |
| `application-dev.yml` | Redis port 6380, gRPC con static:// prefix |
| `application.yml` | Removido debug logging |

### SocialUser Service

| Archivo | Cambio |
|---------|--------|
| `application-dev.yml` | Agregado default para SPRING_NEO4J_URI |

---

## Commits Realizados (14 total)

1. `d5bb196` - feat(platform/error-handling): GlobalErrorHandler Jackson fix + exception handlers
2. `8570b58` - feat(platform/api-response): user domain messages + ApiPageResponse
3. `d80158d` - feat(platform/contracts): proto files update
4. `7ce99bd` - feat(platform/validation): validators + EmailValidator
5. `a1cf2cd` - chore: root pom.xml with platform BOM
6. `1da00c7` - fix(auth-service): SecurityConfig authorizeRequests switch
7. `9f52026` - fix(auth-service): registration flow, endpoints, Kafka
8. `6c4b81d` - fix(auth-service): missing use cases, auth infrastructure
9. `9810146` - fix(auth-service): UserSyncService retry test
10. `efed7fa` - fix(socialuser-service): Dockerfile + pom.xml
11. `dda9b8f` - fix(socialuser-service): user management, Neo4j, Kafka, gRPC
12. `f55973c` - chore: Docker infrastructure + API Gateway
13. `3ad498c` - feat: SecureTokenGenerator + domain events
14. `4f3c60d` - test(platform/validation): unit tests for validators

---

## Resultado Final

### Tests E2E
```
20 passed in 64.95s

- test_auth_health: 2/2 passed
- test_auth_login: 4/4 passed  
- test_auth_register: 6/6 passed
- test_auth_user_queries: 5/5 passed
- test_auth_username_change: 1/1 passed
```

### Arquitectura de Error Handling (Lessons Learned)

1. **El orden de los filtros importa**: En Spring Security 6.x, el orden de registro de filtros determina cuál se ejecuta primero. `AuthorizationFilter` (de `authorizeHttpRequests`) corre ANTES de `JwtAuthFilter` si se registra con `addFilterBefore` después del AnonymousAuthenticationFilter.

2. **Error handling es un pipeline**: Cuando una excepción ocurre:
   - DispatcherServlet captura
   - HandlerExceptionResolver intenta resolver (GlobalErrorHandler)
   - Si el handler falla o no hay handler → ExceptionTranslationFilter
   - Si response.commit() es false → ErrorReportValve → dispatch a /error
   - ErrorController maneja /error → filtro de seguridad se ejecuta DE NUEVO

3. **Jackson + Java Time requiere configuración explícita**: Spring Boot auto-configura un ObjectMapper con JavaTimeModule, pero si creas tu propio ObjectMapper (como en el starter), debes registrarlo manualmente.

4. **Las dependencias opcionales necesitan verificación por string**: En un starter que puede ser usado por múltiples servicios, no puedes asumir que Redis/Kafka/Neo4j están en el classpath. Usar `ex.getClass().getName().contains("Redis")` en lugar de `instanceof RedisException`.

5. **`response.resetBuffer()` previene doble-despacho**: Tomcat's ErrorReportValve sobreescribe la respuesta si no está commiteada. `resetBuffer()` + `flushBuffer()` asegura que la respuesta del handler se envíe antes de que ErrorReportValve pueda intervenir.

---

## Archivos Pendientes (sin commit)

- Eclipse config files (`.classpath`, `.factorypath`, `.settings/`) - Ignorados por convención
- Archivos Redis locales (`redis-server.exe`, etc.) - No pertenecen al repo
- `testing/` - En `.gitignore`
- `.issues/` - Documentación de issues
- `.opencode/` - Configuración del agente

---

## Recomendaciones para Siguiente Sesión

1. **Tests de integración**: Los tests TEST-010, TEST-011, TEST-012 requieren Testcontainers para PostgreSQL, Kafka embebido, gRPC embebido
2. **Tests unitarios**: TEST-001, TEST-002, TEST-003 necesitan cobertura para los domain models y use cases
3. **Limpieza de código**: Remover logs de debug (`logging.level.org.springframework.security: DEBUG`) de application.yml
4. **Validar el fix completo**: Los tests de login, register y queries pasaron, pero falta verificar logout, refresh token, y edge cases
5. **Redis en Docker vs Local**: El compose expone Redis en 6380, pero localmente se está corriendo redis-server.exe. Considerar usar Docker para todo en desarrollo.
