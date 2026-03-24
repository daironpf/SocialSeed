# HIGH PRIORITY ISSUES - SocialSeed

## Issue Summary
These issues significantly impact functionality but don't completely block the system.

---

## AUTH-SERVICE

### [AUTH-010] KafkaPasswordChangedProducer Not Implemented
**Severity:** HIGH  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/messaging/kafka/KafkaPasswordChangedProducer.java`  
**Lines:** 23-42

**Problem:** All Kafka send logic is commented out. The `publish()` method only logs, doesn't actually send.

**Impact:** Password change events not propagated to other services.

---

### [AUTH-011] UserSyncService Recovery Method Signature
**Severity:** HIGH  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/service/UserSyncService.java`  
**Line:** 89

**Problem:** `@Recover` method has incorrect parameters (`oldVal`, `newVal` instead of `oldUsername`, `newUsername`)

---

### [AUTH-012] System.out.println in RegisterUser
**Severity:** HIGH  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/usecase/RegisterUser.java`  
**Lines:** 29-71

**Problem:** Multiple `System.out.println()` calls should be removed or replaced with proper logging.

---

### [AUTH-013] DDL Auto Update in Production
**Severity:** HIGH  
**Service:** auth-service  
**File:** `services/auth-service/src/main/resources/application.yml`  
**Line:** 27

**Problem:**
```yaml
jpa:
  hibernate:
    ddl-auto: update  # Should be 'validate' or use migrations
```

---

## SOCIALUSER-SERVICE

### [SOCIAL-010] ChangeUsername/ChangeEmail Bypass Domain
**Severity:** HIGH  
**Service:** socialuser-service  
**Files:** 
- `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/application/usecase/ChangeUsername.java`
- `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/application/usecase/ChangeEmail.java`

**Problem:** Both use direct repository access instead of going through domain model.

---

### [SOCIAL-011] CreateUser Missing @Transactional
**Severity:** HIGH  
**Service:** socialuser-service  
**File:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/application/usecase/CreateUser.java`

**Problem:** Only use case without `@Transactional` annotation.

---

### [SOCIAL-012] Logger Typo in DeleteUserValidator
**Severity:** HIGH  
**Service:** socialuser-service  
**File:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/application/usecase/validation/DeleteUserValidator.java`  
**Line:** 17

**Problem:**
```java
private static final Logger log = LoggerFactory.getLogger(CreateUserValidator.class);  // Wrong class
```

---

### [SOCIAL-013] Missing application-dev.yml
**Severity:** HIGH  
**Service:** socialuser-service  
**File:** `services/socialuser-service/src/main/resources/application-dev.yml`

**Problem:** No profile for connecting to AuraDB cloud instance.

---

## PLATFORM

### [PLAT-010] Regex Inconsistency in UsernameValidator
**Severity:** HIGH  
**Platform:** socialseed-validation-starter  
**Files:**
- `platform/socialseed-validation-starter/src/main/java/com/socialseed/validation/validator/UsernameRules.java`
- `platform/socialseed-validation-starter/src/main/java/com/socialseed/validation/validator/UsernameValidator.java`

**Problem:**
- `UsernameRules.REGEX` = `"^[a-zA-Z0-9._-]+$"` (allows `.`, `_`, `-`)
- `UsernameValidator.PATTERN` = `"^[a-zA-Z0-9_]{3,20}$"` (only allows `_`)

**Impact:** Contradictory rules - rules say `.` and `-` are allowed, but validator rejects them.

---

### [PLAT-011] Missing Exception Handlers
**Severity:** HIGH  
**Platform:** socialseed-error-handling-starter

**Missing Handlers:**
1. **Neo4jExceptionHandler** - For socialuser-service Neo4j errors
2. **RedisExceptionHandler** - For auth-service Redis errors

---

### [PLAT-012] Missing Kafka Events
**Severity:** HIGH  
**Platform:** socialseed-contracts  
**File:** `platform/socialseed-contracts/src/main/proto/auth_events.proto`

**Missing Events:**
- `AuthPasswordChanged`
- `AuthPasswordReset`
- `AuthAccountLocked`
- `AuthAccountUnlocked`
- `AuthUserLoggedIn`
- `AuthUserLoggedOut`
- `AuthRefreshTokenRevoked`
- `AuthAllSessionsRevoked`

---

## INFRASTRUCTURE

### [INFRA-010] api-gateway Service Missing from docker-compose.yml
**Severity:** HIGH  
**File:** `docker-compose.yml`

**Problem:** api-gateway service defined in pom.xml but not in docker-compose.yml.

---

### [INFRA-011] Neo4j Dependency Commented Out
**Severity:** HIGH  
**File:** `docker-compose.yml`  
**Lines:** 31-33

**Problem:**
```yaml
# depends_on:
#   neo4j:
#     condition: service_healthy
```

**Impact:** socialuser-service can start before Neo4j is ready.

---

### [INFRA-012] Redis Dependency Missing for auth-service
**Severity:** HIGH  
**File:** `docker-compose.yml`

**Problem:** auth-service does NOT depend on redis but requires it for token blacklist.

---

## Priority Order
1. PLAT-011 (missing handlers cause unhandled exceptions)
2. PLAT-012 (incomplete event contracts)
3. INFRA-012 (Redis dependency missing)
4. AUTH-010 (Kafka not working)
5. SOCIAL-010, SOCIAL-011 (architectural violations)
