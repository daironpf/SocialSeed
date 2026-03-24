# CRITICAL ISSUES - SocialSeed

## Issue Summary
These issues require immediate attention as they block core functionality or pose significant risks.

---

## AUTH-SERVICE

### [AUTH-001] ChangeUserPasswordValidator - Inverted Logic
**Severity:** CRITICAL  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/usecase/validation/ChangeUserPasswordValidator.java`  
**Lines:** 28-30

**Problem:**
```java
// Current code (INCORRECT):
if (!passwordEncoder.matches(request.getCurrentPassword(), authUser.getPasswordHash())) {
    throw new BusinessException(ErrorCode.PASSWORD_MISMATCH, HttpStatus.BAD_REQUEST);
}
```
The condition throws exception when password IS valid (matches), should throw when it DOESN'T match.

**Expected:**
```java
if (passwordEncoder.matches(request.getCurrentPassword(), authUser.getPasswordHash())) {
    throw new BusinessException(ErrorCode.PASSWORD_MISMATCH, HttpStatus.BAD_REQUEST);
}
```

**Impact:** Users cannot change their password - the validator always fails.

---

### [AUTH-002] Hardcoded Admin UUID in RoleController
**Severity:** CRITICAL  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/entry/rest/controller/RoleController.java`  
**Lines:** 72, 95

**Problem:** Hardcoded UUID `00000000-0000-0000-0000-000000000001` for admin operations.

**Recommendation:** Use configuration property or environment variable.

---

### [AUTH-003] JwtAuthFilter Not Registered in SecurityConfig
**Severity:** CRITICAL  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/config/security/JwtAuthFilter.java`

**Problem:** JwtAuthFilter is not annotated with `@Component` and not registered in SecurityConfig filter chain.

**Impact:** JWT authentication is not being applied to protected endpoints.

---

### [AUTH-004] ValidationService.get() Without Null Check
**Severity:** CRITICAL  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/service/ValidationService.java`  
**Line:** 32

**Problem:**
```java
authUserRepository.findById(userId).get()  // Can throw NoSuchElementException
```

**Recommendation:** Use `.orElseThrow(() -> new BusinessException(...))`

---

### [AUTH-005] RegisterUser Empty Catch Block
**Severity:** CRITICAL  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/usecase/RegisterUser.java`  
**Lines:** 65-66

**Problem:** Catches all exceptions silently and continues with random UUID - could create inconsistent state.

---

## PLATFORM

### [PLAT-001] PGSQLExceptionHandler Not Annotated as ControllerAdvice
**Severity:** CRITICAL  
**Platform:** socialseed-error-handling-starter  
**File:** `platform/socialseed-error-handling-starter/src/main/java/com/socialseed/errorhandling/exceptionhandler/PGSQLExceptionHandler.java`

**Problem:** Class is missing `@RestControllerAdvice` annotation. Spring won't scan it as exception handler.

**Impact:** PostgreSQL integrity violations not handled properly.

---

### [PLAT-002] Hardcoded JWT Secret
**Severity:** CRITICAL  
**Service:** auth-service  
**File:** `services/auth-service/src/main/resources/application.yml`  
**Line:** 60

**Problem:**
```yaml
jwt:
  secret: "mi-super-clave-ultra-segura-de-64-caracteres-2025-1234567890"
```

**Recommendation:** Use environment variable: `${JWT_SECRET}`

---

## SOCIALUSER-SERVICE

### [SOCIAL-001] KafkaDomainEventPublisher Empty Implementation
**Severity:** CRITICAL  
**Service:** socialuser-service  
**File:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/infrastructure/messaging/kafka/KafkaDomainEventPublisher.java`

**Problem:** Class is completely empty (only has comment `// implementa publisher`)

**Impact:** Domain events cannot be published to Kafka.

---

### [SOCIAL-002] Missing Domain Events
**Severity:** CRITICAL  
**Service:** socialuser-service  
**Location:** No `domain/event` package exists

**Problem:** No domain event classes defined for user lifecycle events.

**Recommendation:** Create events:
- `SocialUserCreatedEvent`
- `SocialUserProfileUpdatedEvent`
- `SocialUserVacationStartedEvent`
- `SocialUserVacationEndedEvent`

---

## INFRASTRUCTURE

### [INFRA-001] api-gateway Route Port Mismatch
**Severity:** CRITICAL  
**File:** `services/api-gateway/src/main/resources/application.yml`

**Problem:**
```yaml
routes:
  - id: socialuser-route
    uri: http://socialuser-service:4000  # WRONG - should be 8090
```

**Impact:** API Gateway cannot reach socialuser-service.

---

### [INFRA-002] Kafka Consumers Disabled
**Severity:** CRITICAL  
**File:** `services/socialuser-service/src/main/resources/application.yml`

**Problem:**
```yaml
kafka:
  listener:
    auto-startup: false  # CONSUMERS ARE DISABLED!
```

**Impact:** socialuser-service won't consume Kafka events.

---

## Priority Order
1. AUTH-001 (blocks password change)
2. AUTH-003 (blocks JWT authentication)
3. PLAT-001 (database errors not handled)
4. INFRA-002 (Kafka consumers disabled)
5. SOCIAL-001, SOCIAL-002 (event-driven architecture broken)
