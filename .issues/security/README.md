# SECURITY ISSUES - SocialSeed

## Issue Summary
Security vulnerabilities and hardening requirements.

---

## [SEC-001] Hardcoded JWT Secret
**Severity:** CRITICAL  
**Service:** auth-service  
**File:** `services/auth-service/src/main/resources/application.yml`  
**Line:** 60

**Problem:**
```yaml
jwt:
  secret: "mi-super-clave-ultra-segura-de-64-caracteres-2025-1234567890"
```

**Risk:** Secret exposed in source code.

**Recommendation:** Use environment variable:
```yaml
jwt:
  secret: ${JWT_SECRET}
```

---

## [SEC-002] Hardcoded Credentials in docker-compose.yml
**Severity:** CRITICAL  
**File:** `docker-compose.yml`

**Problems:**
```yaml
# Neo4j
NEO4J_AUTH: neo4j/neoSocial

# PostgreSQL
POSTGRES_USER: authuser
POSTGRES_PASSWORD: authpass
```

**Risk:** Credentials in source control.

**Recommendation:** Use Docker secrets or environment variable files.

---

## [SEC-003] JWT Token Not Invalidated on Logout
**Severity:** HIGH  
**Service:** auth-service

**Problem:** `logout` and `revokeAllTokensForUser` don't invalidate existing JWTs, only refresh tokens.

**Impact:** Tokens can still be used until expiration after logout.

**Recommendation:** Implement token blacklist in Redis or use short-lived tokens.

---

## [SEC-004] No Brute Force Protection
**Severity:** HIGH  
**Service:** auth-service  
**File:** `LoginController.java`

**Problem:** No rate limiting on login endpoint. Attackers can attempt unlimited passwords.

**Recommendation:** Implement login attempt throttling with Redis.

---

## [SEC-005] CSRF Disabled Without Token Handling
**Severity:** HIGH  
**Service:** auth-service  
**File:** `SecurityConfig.java`

**Problem:** CSRF disabled but no CSRF token handling for browser clients.

**Recommendation:** Either enable CSRF or implement CSRF token generation.

---

## [SEC-006] No Redis Authentication
**Severity:** MEDIUM  
**File:** `docker-compose.yml`

**Problem:**
```yaml
redis:
  command: redis-server --maxmemory 128mb --maxmemory-policy allkeys-lru
  # Missing: --requirepass <password>
```

**Risk:** Anyone can access Redis data.

---

## [SEC-007] No TLS/SSL Configuration
**Severity:** MEDIUM  
**Files:** `docker-compose.yml`, application configurations

**Missing:**
- Kafka: PLAINTEXT only, no SSL
- Neo4j: No TLS for bolt connection
- PostgreSQL: No SSL required

**Recommendation:** Enable TLS for all connections in production.

---

## [SEC-008] Verification Tokens Not Cryptographically Random
**Severity:** MEDIUM  
**Service:** auth-service  
**File:** `RegisterUser.java`

**Problem:** Uses `UUID.randomUUID()` instead of `SecureRandom`.

**Recommendation:** Use `java.security.SecureRandom` for token generation.

---

## [SEC-009] No Token Rotation on Role Change
**Severity:** MEDIUM  
**Service:** auth-service

**Problem:** User roles can change but existing JWT tokens don't reflect the change.

**Recommendation:** Implement token invalidation on role changes.

---

## [SEC-010] Hardcoded Admin UUID
**Severity:** MEDIUM  
**Service:** auth-service  
**File:** `RoleController.java`  
**Lines:** 72, 95

**Problem:** Hardcoded `UUID.fromString("00000000-0000-0000-0000-000000000001")` for admin operations.

**Recommendation:** Use configuration property or environment variable.

---

## [SEC-011] No Network Policies
**Severity:** LOW  
**File:** `docker-compose.yml`

**Problem:** Services can communicate freely without network restrictions.

**Recommendation:** Implement Docker network policies to restrict inter-service communication.

---

## Priority Order
1. SEC-001 (JWT secret exposed)
2. SEC-002 (credentials exposed)
3. SEC-004 (brute force attack)
4. SEC-005 (CSRF vulnerability)
5. SEC-003 (session invalidation)
6. SEC-006, SEC-007 (transport security)
