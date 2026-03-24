# LOW PRIORITY ISSUES - SocialSeed

## AUTH-SERVICE

### [AUTH-030] RefreshTokenRequestDTO Validates as UUID
**Severity:** LOW  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/entry/rest/dto/request/RefreshTokenRequestDTO.java`

**Problem:** Refresh tokens are UUID strings, not standard UUID format - may cause validation failures.

---

### [AUTH-031] Duplicate DTO Exists
**Severity:** LOW  
**Service:** auth-service  
**Files:**
- `ChangePasswordRequestDTO.java`
- `PasswordChangeRequest.java`

**Problem:** Similar fields exist in two different classes.

---

### [AUTH-032] Logout Returns Empty Body
**Severity:** LOW  
**Service:** auth-service  
**File:** `services/auth-service/src/main/java/com/socialseed/authservice/auth/entry/rest/controller/LogoutController.java`

**Problem:** Returns 204 but response body handling inconsistent with ApiResponse format.

---

## SOCIALUSER-SERVICE

### [AUTH-033] Testcontainers Config Issue
**Severity:** LOW  
**Service:** socialuser-service  
**File:** `services/socialuser-service/src/test/java/com/socialseed/socialuser/testconfig/Neo4jIntegrationTest.java`

**Problem:** Test uses `withoutAuthentication()` but production requires auth.

---

### [SOCIAL-030] Commented Out Code in UserController
**Severity:** LOW  
**Service:** socialuser-service  
**File:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/entry/rest/controller/UserController.java`

**Problem:** Multiple commented out endpoints and error responses should be removed.

---

### [SOCIAL-031] UserCreateRequestDTO Contains Password
**Severity:** LOW  
**Service:** socialuser-service  
**File:** `services/socialuser-service/src/main/java/com/socialseed/socialuser/user/entry/rest/dto/request/UserCreateRequestDTO.java`  
**Lines:** 17-18

**Problem:** Contains `password` field - should not be in socialuser-service.

---

## PLATFORM

### [PLAT-030] @ValidRole Missing Braces in Message
**Severity:** LOW  
**Platform:** socialseed-validation-starter  
**File:** `platform/socialseed-validation-starter/src/main/java/com/socialseed/validation/annotation/ValidRole.java`  
**Line:** 14

**Problem:** Message default uses raw i18n key without curly braces `{}`.

---

### [PLAT-031] ResponseDTO Redundant Class
**Severity:** LOW  
**Platform:** socialseed-api-response-starter

**Problem:** Both `ApiResponse<T>` and `ResponseDTO` exist. `ResponseDTO` is redundant and less feature-rich.

---

### [PLAT-032] Duplicate Response Factory Methods
**Severity:** LOW  
**Platform:** socialseed-api-response-starter  
**File:** `platform/socialseed-api-response-starter/src/main/java/com/socialseed/apiresponse/ApiResponse.java`

**Problem:** Inconsistent factory method patterns (`success` vs `message`).

---

### [PLAT-033] Missing ApiMessageKey Coverage
**Severity:** LOW  
**Platform:** socialseed-api-response-starter

**Missing Keys:**
- `UPDATED`
- `DELETED`
- `BAD_REQUEST`
- `UNAUTHORIZED`
- `FORBIDDEN`
- `NOT_FOUND`
- `CONFLICT`
- `INTERNAL_ERROR`

---

## INFRASTRUCTURE

### [INFRA-030] Dockerfile Build Context Inconsistency
**Severity:** LOW  
**Files:**
- `services/api-gateway/Dockerfile` - Single service context
- `services/auth-service/Dockerfile` - Root context

**Problem:** Different build context patterns between services.

---

### [INFRA-031] gRPC Port Not Exposed in Dockerfile
**Severity:** LOW  
**Service:** socialuser-service  
**File:** `services/socialuser-service/Dockerfile`

**Problem:** Only exposes port 8090 (REST), but gRPC runs on port 9090.

---

### [INFRA-032] Extra Spanish Translation Key
**Severity:** LOW  
**Platform:** socialseed-api-response-starter  
**File:** `messages_es.properties`

**Problem:** `user.username.size=El username no debe superar los 30 caracteres` not in other languages.

---

## Priority Order
1. PLAT-031 (cleanup redundant code)
2. INFRA-031 (Docker port exposure)
3. AUTH-030 (minor validation issue)
4. SOCIAL-030 (code cleanup)
5. PLAT-032 (API consistency)
