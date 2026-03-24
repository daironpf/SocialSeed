# MEDIUM PRIORITY ISSUES - SocialSeed

## AUTH-SERVICE

### [AUTH-020] Double Optional Wrapping in GetUserById
**Severity:** MEDIUM  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/usecase/GetUserById.java`  
**Line:** 21

**Problem:** `Optional.ofNullable(repository.findById(...))` wraps an already Optional result.

---

### [AUTH-021] Hardcoded gRPC Static Prefix
**Severity:** MEDIUM  
**Service:** auth-service  
**File:** `services/auth-service/src/main/resources/application-docker.yml`

**Problem:**
```yaml
address: ${SPRING_GRPC_CLIENT_CHANNELS_USER_ADDRESS:static://socialuser-service:9090}
```
The `static://` prefix is incorrect for Spring gRPC.

---

### [AUTH-022] Duplicate PasswordEncoder Beans
**Severity:** MEDIUM  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/config/security/UserSecurityConfig.java`

**Problem:** Both UserSecurityConfig and Spring Security auto-config create PasswordEncoder - potential conflict.

---

### [AUTH-023] Kafka Topic Inconsistency
**Severity:** MEDIUM  
**Service:** auth-service  
**Files:**
- `KafkaUserRegisteredProducer.java` - hardcoded `"auth.user.registered"`
- `application.yml` - config uses `kafka.topic.auth-user-registered`

---

### [AUTH-024] Duplicate Token Cleanup
**Severity:** MEDIUM  
**Service:** auth-service  
**Files:**
- `TokenCleanupScheduler.java`
- `TokenCleanupJob.java`

**Problem:** Both clean tokens on different schedules - redundant.

---

## SOCIALUSER-SERVICE

### [SOCIAL-020] REST Controller Optional Handling
**Severity:** MEDIUM  
**Service:** socialuser-service  
**File:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/entry/rest/controller/UserController.java`  
**Line:** 58

**Problem:** Calls `.get()` without checking `isPresent()` - will throw `NoSuchElementException`.

---

### [SOCIAL-021] Inconsistent @Transactional Usage
**Severity:** MEDIUM  
**Service:** socialuser-service

**Problem:**
- **Without @Transactional:** CreateUser, DeleteUser, GetUserById, GetUserByEmail, GetUserByName, GetAllUsers
- **With @Transactional:** UpdateUserProfile, StartVacation, EndVacation, ChangeUsername, ChangeEmail

**Recommendation:** Review each use case for proper transaction boundaries.

---

### [SOCIAL-022] Kafka Topics Not Versioned
**Severity:** MEDIUM  
**Service:** socialuser-service  
**File:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/entry/event/consumer/AuthEventsConsumer.java`

**Problem:** Uses hardcoded topics without versioning (e.g., `auth.user.username.changed` should be `auth.user.username.changed.v1`).

---

### [SOCIAL-023] Commented Out REST Endpoints
**Severity:** MEDIUM  
**Service:** socialuser-service  
**File:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/entry/rest/controller/UserController.java`  
**Lines:** 81-84, 102-104, 111-116

**Problem:** Proper 404 responses are commented out, returns 200 with null instead.

---

## PLATFORM

### [PLAT-020] @ValidUsername Hardcoded Message
**Severity:** MEDIUM  
**Platform:** socialseed-validation-starter  
**File:** `platform/socialseed-validation-starter/src/main/java/com/socialseed/validation/annotation/ValidUsername.java`  
**Line:** 15

**Problem:** Default message is hardcoded in Spanish instead of using i18n key.

---

### [PLAT-021] Missing @ValidEmail Annotation
**Severity:** MEDIUM  
**Platform:** socialseed-validation-starter

**Problem:** Email validation uses standard `@Email` from jakarta. Should have platform-level `@ValidEmail` with consistent i18n support.

---

### [PLAT-022] Missing Kafka Exception Handler
**Severity:** MEDIUM  
**Platform:** socialseed-error-handling-starter

**Missing Handlers:**
- `KafkaException`
- `ProducerException`
- `ConsumerException`

---

### [PLAT-023] No Pagination Support in ApiResponse
**Severity:** MEDIUM  
**Platform:** socialseed-api-response-starter

**Problem:** No `ApiPageResponse` or similar for paginated endpoints.

---

### [PLAT-024] Missing Security Exception Handlers
**Severity:** MEDIUM  
**Platform:** socialseed-error-handling-starter

**Missing Handlers:**
- `AccessDeniedException`
- `AuthenticationException`
- `JwtException`

---

## INFRASTRUCTURE

### [INFRA-020] Kafka Topics Not Initialized in Docker
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:** `infrastructure/kafka/topics-init.sh` script exists but is not mounted or executed in the Kafka container.

---

### [INFRA-021] No Healthchecks for api-gateway and auth-service
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:** api-gateway and auth-service missing health check definitions.

---

### [INFRA-022] Windows-Specific Volume Paths
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:**
```yaml
volumes:
  - D:/db_volumes/neo4j_data:/var/lib/neo4j/data
```

**Recommendation:** Use relative paths: `./infrastructure/volumes/neo4j_data`

---

### [INFRA-023] No Redis Persistence
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:** Redis data is NOT persisted - will be lost on container recreation.

---

### [INFRA-024] No Resource Limits
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:** No memory or CPU limits configured for services.

---

## Priority Order
1. PLAT-024 (security exceptions not handled)
2. INFRA-024 (no resource limits - potential DoS)
3. AUTH-020, SOCIAL-020 (runtime exceptions)
4. PLAT-022 (Kafka errors not handled)
5. INFRA-021 (missing health checks)
