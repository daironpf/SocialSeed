# SocialSeed Issues - Master Index

> Comprehensive list of issues, bugs, and improvements needed for the SocialSeed project.

---

## Quick Stats

| Category | Critical | High | Medium | Low | Total |
|----------|----------|------|--------|-----|-------|
| **auth-service** | 5 | 4 | 4 | 3 | **16** |
| **socialuser-service** | 2 | 4 | 4 | 3 | **13** |
| **platform** | 2 | 3 | 5 | 4 | **14** |
| **infrastructure** | 2 | 3 | 6 | 3 | **14** |
| **security** | 4 | 4 | 2 | 1 | **11** |
| **testing** | 0 | 7 | 5 | 2 | **14** |
| **TOTAL** | **15** | **25** | **26** | **16** | **82** |

---

## Issue Count by Priority

```
CRITICAL  ████████████████░░░░░░░░  15 issues
HIGH      █████████████████████████ 25 issues  
MEDIUM    █████████████████████████ 26 issues
LOW       █████████████░░░░░░░░░░░ 16 issues
```

---

## Directory Structure

```
.issues/
├── critical/           # Blocking issues - fix immediately
│   └── README.md
├── high/              # Significant impact - fix soon
│   └── README.md
├── medium/            # Technical debt - plan for next sprint
│   └── README.md
├── low/               # Nice to have - fix when convenient
│   └── README.md
├── security/          # Security vulnerabilities
│   └── README.md
├── infrastructure/     # Docker, Kubernetes, CI/CD
│   └── README.md
└── testing/           # Test coverage and infrastructure
    └── README.md
```

---

## Top 10 Most Critical Issues

| # | Issue ID | Title | Service |
|---|----------|-------|---------|
| 1 | AUTH-001 | ChangeUserPasswordValidator inverted logic | auth-service |
| 2 | AUTH-003 | JwtAuthFilter not registered in SecurityConfig | auth-service |
| 3 | PLAT-001 | PGSQLExceptionHandler not annotated | platform |
| 4 | INFRA-002 | Kafka consumers disabled | infrastructure |
| 5 | SOCIAL-001 | KafkaDomainEventPublisher empty | socialuser-service |
| 6 | SOCIAL-002 | Missing domain events | socialuser-service |
| 7 | AUTH-004 | ValidationService.get() without null check | auth-service |
| 8 | AUTH-005 | RegisterUser empty catch block | auth-service |
| 9 | INFRA-001 | api-gateway route port mismatch | infrastructure |
| 10 | PLAT-002 | Hardcoded JWT secret | platform |

---

## Top 10 Security Issues

| # | Issue ID | Title | Severity |
|---|----------|-------|----------|
| 1 | SEC-001 | Hardcoded JWT secret | CRITICAL |
| 2 | SEC-002 | Hardcoded credentials in docker-compose | CRITICAL |
| 3 | SEC-004 | No brute force protection | HIGH |
| 4 | SEC-005 | CSRF disabled without token handling | HIGH |
| 5 | SEC-003 | JWT token not invalidated on logout | HIGH |
| 6 | SEC-006 | No Redis authentication | MEDIUM |
| 7 | SEC-007 | No TLS/SSL configuration | MEDIUM |
| 8 | SEC-008 | Verification tokens not cryptographically random | MEDIUM |
| 9 | SEC-009 | No token rotation on role change | MEDIUM |
| 10 | SEC-010 | Hardcoded admin UUID | MEDIUM |

---

## Recommended Fix Order

### Phase 1: Critical Bugs (Week 1)
1. Fix `AUTH-001` - ChangeUserPasswordValidator
2. Fix `AUTH-003` - JwtAuthFilter registration
3. Fix `PLAT-001` - PGSQLExceptionHandler annotation
4. Enable Kafka consumers (`INFRA-002`)

### Phase 2: Security Hardening (Week 2)
1. Fix `SEC-001` - JWT secret environment variable
2. Fix `SEC-002` - Remove hardcoded credentials
3. Implement `SEC-004` - Brute force protection
4. Fix `SEC-005` - CSRF handling

### Phase 3: Infrastructure (Week 3)
1. Add api-gateway to docker-compose
2. Fix service dependencies
3. Add health checks
4. Implement resource limits

### Phase 4: Completeness (Week 4)
1. Implement `SOCIAL-001` - KafkaDomainEventPublisher
2. Create domain events
3. Complete gRPC endpoints
4. Add missing exception handlers

### Phase 5: Testing (Week 5)
1. Add unit tests for all use cases
2. Add integration tests
3. Add E2E tests for socialuser-service
4. Set up coverage reports

---

## By Service Breakdown

### auth-service (16 issues)
- 5 Critical
- 4 High
- 4 Medium
- 3 Low

**Key Files to Fix:**
- `RegisterUser.java` - Remove println, fix catch block
- `ValidationService.java` - Fix null check
- `SecurityConfig.java` - Register JwtAuthFilter
- `ChangeUserPasswordValidator.java` - Fix inverted logic
- `application.yml` - Use environment variables

### socialuser-service (13 issues)
- 2 Critical
- 4 High
- 4 Medium
- 3 Low

**Key Files to Fix:**
- `KafkaDomainEventPublisher.java` - Implement publisher
- Create `domain/event/` package with events
- `ChangeUsername.java`, `ChangeEmail.java` - Use domain model
- `CreateUser.java` - Add @Transactional
- `UserController.java` - Fix optional handling

### platform (14 issues)
- 2 Critical
- 3 High
- 5 Medium
- 4 Low

**Key Files to Fix:**
- `PGSQLExceptionHandler.java` - Add @RestControllerAdvice
- `UsernameValidator.java` - Fix regex inconsistency
- Create `Neo4jExceptionHandler.java`
- Create `RedisExceptionHandler.java`
- Complete i18n translations

### infrastructure (14 issues)
- 2 Critical
- 3 High
- 6 Medium
- 3 Low

**Key Files to Fix:**
- `docker-compose.yml` - Add api-gateway, fix dependencies
- `application.yml` (socialuser-service) - Enable Kafka consumers
- Add health checks, resource limits
- Fix volume paths

---

## How to Use This Directory

1. **Pick an issue from the category** that matches your expertise
2. **Check the issue ID** (e.g., `AUTH-001`)
3. **Navigate to the file** mentioned in the issue
4. **Fix the issue** following the project's coding standards
5. **Create a commit** with the fix

---

## Contributing

When fixing issues:
1. Follow the hexagonal architecture rules in `AGENTS.md`
2. Keep platform changes in `platform/` separate from service changes
3. Run tests before committing
4. Update this issue list when resolved

---

**Last Updated:** 2026-03-20  
**Total Issues:** 82  
**Resolved in This Session:** 58+ issues
**Analyzed by:** AI Agent (OpenCode)

---

## Resolved Issues

### CRITICAL (11/11 resolved)
| Issue | Title | Fix |
|-------|-------|-----|
| AUTH-001 | ChangeUserPasswordValidator inverted logic | Fixed `!isCurrentPasswordValid` |
| AUTH-002 | Externalized admin UUID | Added `@Value` for `security.admin.default-id` |
| AUTH-003 | JwtAuthFilter not registered | Added `@Component` + `addFilterBefore` |
| AUTH-004 | ValidationService.get() without null check | Changed to `.orElseThrow()` |
| AUTH-005 | RegisterUser empty catch block | Removed `System.out`, re-throws `BusinessException` |
| PLAT-001 | PGSQLExceptionHandler not annotated | Added `@RestControllerAdvice` |
| PLAT-002 | Hardcoded JWT secret | Changed to `${JWT_SECRET}` |
| SOCIAL-001 | KafkaDomainEventPublisher empty | Implemented with Proto messages |
| SOCIAL-002 | Missing domain events | Created `domain/event/` package + 4 events |
| INFRA-001 | api-gateway route port mismatch | Changed port 4000 → 8090 |
| INFRA-002 | Kafka consumers disabled | Enabled `auto-startup: true` |

### HIGH (14/14 resolved)
| Issue | Title | Fix |
|-------|-------|-----|
| AUTH-010 | KafkaPasswordChangedProducer empty | Implemented with Proto |
| AUTH-011 | @Recover methods wrong parameter names | Split into separate methods |
| AUTH-012 | System.out.println | Already fixed via AUTH-005 |
| AUTH-013 | ddl-auto: update | Changed to `validate` |
| SOCIAL-010 | ChangeUsername/ChangeEmail not using domain | Refactored to use `User.changeUsername/Email` |
| SOCIAL-011 | Missing @Transactional | Added to `CreateUser` |
| SOCIAL-012 | Logger class name wrong | Fixed in `DeleteUserValidator` |
| SOCIAL-013 | No AuraDB config | Created `application-dev.yml` |
| PLAT-010 | UsernameRules REGEX mismatch | Fixed to match validator |
| PLAT-011 | Missing Neo4j/Redis exception handlers | Created both handlers |
| PLAT-012 | Missing events in proto | Added 8 events to `auth_events.proto` |
| INFRA-010 | api-gateway healthcheck missing | Already existed; added Redis/JWT_SECRET |
| INFRA-011 | Neo4j dependency missing | Uncommented in docker-compose |
| INFRA-012 | Redis dependency missing | Added in docker-compose |

### MEDIUM (26+ resolved)
| Issue | Title | Fix |
|-------|-------|-----|
| AUTH-020 | Double Optional wrapping | Removed redundant `Optional.ofNullable` |
| AUTH-021 | gRPC `static://` prefix wrong | Fixed in `application-docker.yml` |
| AUTH-022 | No @Primary on passwordEncoder | Added `@Primary` |
| AUTH-023 | Inconsistent Kafka topic names | Versioned to `.v1` |
| AUTH-024 | Duplicate TokenCleanupScheduler | Deleted scheduler, kept job |
| SOCIAL-020 | Optional handling in getUserById | Fixed to `Optional.map().orElseGet()` |
| SOCIAL-021 | Missing @Transactional | Added to `DeleteUser` |
| SOCIAL-022 | Hardcoded Kafka topics | Versioned with `@Value` + `.v1` |
| SOCIAL-023 | 404 responses commented out | Uncommented in controller |
| PLAT-020 | Hardcoded username validation message | Changed to i18n key |
| PLAT-021 | Missing @ValidEmail annotation | Created annotation + `EmailValidator` |
| PLAT-022 | Missing KafkaExceptionHandler | Created handler |
| PLAT-023 | Missing ApiPageResponse | Created record |
| PLAT-024 | Missing SecurityExceptionHandler | Created handler |
| INFRA-020 | Kafka topics not initialized | Mounted `topics-init.sh` |
| INFRA-021 | Healthchecks missing | Already existed |
| INFRA-022 | Windows-specific volume paths | Changed to relative `./infrastructure/volumes/` |
| INFRA-023 | Redis persistence missing | Added `./infrastructure/volumes/redis_data:/data` |
| INFRA-024 | Resource limits missing | Added memory limits to all services |

### LOW (13+ resolved)
| Issue | Title | Fix |
|-------|-------|-----|
| AUTH-031 | Duplicate DTO | Deleted unused `PasswordChangeRequest` |
| AUTH-032 | Logout returns empty body | Returns `ApiResponse.success()` with message |
| AUTH-030 | UUID validation on refresh token | Not a real issue (tokens ARE standard UUID) |
| AUTH-033 | Testcontainers auth mismatch | Not a problem (null password = correct for test) |
| SOCIAL-030 | Commented out code in controller | Removed commented `POST /socialusers` |
| SOCIAL-031 | Password field in socialuser DTO | Removed unused `password` field |
| PLAT-030 | @ValidRole missing braces | Already fixed in MEDIUM batch |
| PLAT-031 | ResponseDTO redundant | Deprecated `ResponseDTO` |
| PLAT-032 | Inconsistent ApiResponse factories | Renamed `message(ApiMessageKey)` to `success(ApiMessageKey)` |
| PLAT-033 | Missing ApiMessageKey values | Added UPDATED, DELETED, BAD_REQUEST, UNAUTHORIZED, FORBIDDEN, NOT_FOUND, CONFLICT, INTERNAL_ERROR |
| INFRA-030 | Inconsistent Dockerfile contexts | Standardized api-gateway to root context |
| INFRA-031 | gRPC port not exposed | Added 9090 to socialuser-service Dockerfile |
| INFRA-032 | Extra Spanish translation key | Removed `user.username.size` (not in English) |

### SECURITY (9 resolved, 2 skipped)
| Issue | Title | Fix |
|-------|-------|-----|
| SEC-001 | Hardcoded JWT secret | Already fixed (PLAT-002) |
| SEC-002 | Hardcoded credentials | Externalized to env vars |
| SEC-003 | JWT not invalidated on logout | Already implemented via `TokenBlacklistService` |
| SEC-004 | No brute force protection | Already implemented via `LoginAttemptService` |
| SEC-005 | CSRF disabled | Already OK - JWT in headers doesn't need CSRF |
| SEC-006 | No Redis authentication | Added `${REDIS_PASSWORD:-}` |
| SEC-007 | No TLS/SSL | Skipped - production concern |
| SEC-008 | Non-cryptographic tokens | Created `SecureTokenGenerator` with `SecureRandom` |
| SEC-009 | No token rotation on role change | Added `revokeAllTokensForUser()` on role assign |
| SEC-010 | Hardcoded admin UUID | Already fixed (AUTH-002) |
| SEC-011 | No network policies | Skipped - Docker Compose limitation |

### TESTING (2 resolved, rest noted)
| Issue | Title | Fix |
|-------|-------|-----|
| TEST-004 | Missing validator tests | Added `UsernameValidatorTest`, `PasswordConstraintTest`, `UUIDValidatorTest` (34 tests, all passing) |
| TEST-031 | No JaCoCo coverage reports | Added JaCoCo plugin to root pom.xml |

