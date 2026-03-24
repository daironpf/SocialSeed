# TESTING ISSUES - SocialSeed

## Issue Summary
Test coverage gaps and testing infrastructure improvements needed.

---

## MISSING UNIT TESTS

### [TEST-001] No Unit Tests for auth-service Domain Layer
**Severity:** HIGH  
**Service:** auth-service

**Missing Test Coverage:**
- `AuthUser` domain model
- `RefreshToken` domain model
- `AuthUserService` domain service
- Role validation logic

**Recommendation:** Create tests in `src/test/java/` following the hexagonal architecture.

---

### [TEST-002] No Unit Tests for auth-service Use Cases
**Severity:** HIGH  
**Service:** auth-service

**Missing Tests:**
- `RegisterUser`
- `LoginUser`
- `ChangePassword`
- `ChangeUsername`
- `GetUserById`
- `RefreshToken`
- `Logout`

**Recommendation:** Create comprehensive unit tests with Mockito.

---

### [TEST-003] No Unit Tests for socialuser-service Use Cases
**Severity:** HIGH  
**Service:** socialuser-service

**Missing Tests:**
- `CreateUser`
- `GetUserById`
- `GetUserByEmail`
- `GetUserByName`
- `GetAllUsers`
- `DeleteUser`
- `UpdateUserProfile`
- `StartVacation`
- `EndVacation`
- `ChangeUsername`
- `ChangeEmail`

---

### [TEST-004] No Unit Tests for Platform Validators
**Severity:** MEDIUM  
**Platform:** socialseed-validation-starter

**Missing Tests:**
- `UsernameValidatorTest`
- `PasswordConstraintTest`
- `UUIDValidatorTest`

**Existing:** Only `RoleValidatorTest` exists.

---

## INTEGRATION TESTS

### [TEST-010] Testcontainers Configuration Issues
**Severity:** MEDIUM  
**Services:** auth-service, socialuser-service

**Problem:** Test uses `withoutAuthentication()` but production requires auth.

**Impact:** Tests may pass but production fails.

---

### [TEST-011] No Integration Tests for Kafka Producers
**Severity:** MEDIUM  
**Services:** auth-service, socialuser-service

**Problem:** No tests for Kafka event publishing with embedded Kafka.

---

### [TEST-012] No Integration Tests for gRPC Endpoints
**Severity:** MEDIUM  
**Services:** socialuser-service

**Problem:** gRPC endpoints not tested with embedded gRPC.

---

## E2E TESTS

### [TEST-020] E2E Tests in Wrong Directory
**Severity:** MEDIUM  
**File:** `testing/tests/`

**Problem:** E2E tests exist in `testing/` but `socialseed-e2e` framework expects `services/` directory.

**Recommendation:** Either:
1. Move tests to `services/auth/services/` for socialseed-e2e framework
2. Or keep as pytest tests (current approach is fine for REST APIs)

---

### [TEST-021] No E2E Tests for socialuser-service
**Severity:** MEDIUM  
**Location:** `testing/`

**Problem:** Only auth-service E2E tests exist.

**Missing:**
- Profile CRUD operations
- Vacation management
- gRPC integration tests

---

### [TEST-022] No E2E Tests for Nexus Service
**Severity:** MEDIUM  
**Location:** `testing/`

**Problem:** Nexus service has no E2E tests despite being in active development.

---

## TEST CONFIGURATION

### [TEST-030] conftest.py Type Error
**Severity:** LOW  
**File:** `testing/tests/conftest.py`  
**Lines:** 15, 19

**Problem:**
```
ERROR: Return type of generator function must be compatible with "Generator[AuthPage, Any, Any]"
```

**Fix:** Change return type hint or use proper pytest generator syntax.

---

### [TEST-031] No Test Coverage Reports
**Severity:** MEDIUM  
**Location:** All services

**Problem:** No JaCoCo or similar coverage reports configured.

**Recommendation:** Add to `pom.xml`:
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
</plugin>
```

---

## TESTING INFRASTRUCTURE

### [TEST-040] No CI/CD Test Pipeline
**Severity:** MEDIUM  
**Location:** `.github/workflows/`

**Problem:** GitHub Actions workflow exists but doesn't run all test types.

**Recommendation:** Create pipeline:
1. Unit tests (all services)
2. Integration tests (with Testcontainers)
3. E2E tests (pytest + socialseed-e2e)

---

### [TEST-041] No Performance/Load Tests
**Severity:** LOW  
**Location:** `testing/`

**Problem:** No performance tests for high load scenarios.

---

## Priority Order
1. TEST-001, TEST-002, TEST-003 (coverage gaps)
2. TEST-010 (test reliability)
3. TEST-011, TEST-012 (integration coverage)
4. TEST-040 (CI/CD pipeline)
5. TEST-020, TEST-021 (E2E coverage)
