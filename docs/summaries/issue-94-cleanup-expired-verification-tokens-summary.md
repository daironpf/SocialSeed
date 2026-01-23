# Summary: Cleanup Expired Email Verification Tokens (Issue #94)

## Overview
Implemented an automated cleanup mechanism for expired email verification tokens. This task was consolidated with Issue #95 to provide a unified maintenance job for all security-related tokens.

## Changes Implemented

### Domain & Persistence
- **Repository Interface**: Updated `AuthUserRepository` with the `clearExpiredEmailVerificationTokens` method.
- **JPA Implementation**: Added an optimized `@Modifying` update query to `AuthUserPgsqlRepository` to nullify expired verification tokens where the email has not yet been verified.

### Infrastructure
- **Scheduler**: Integrated the verification token cleanup into the `TokenCleanupScheduler`.
- **Configuration**: The job runs hourly by default, ensuring the database remains clean of stale security tokens.

### Verification
- **Automated Tests**: The `TokenCleanupIntegrationTest` verifies that expired verification tokens are nulled for unverified users while valid ones are preserved.

## Files Involved
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/domain/repository/AuthUserRepository.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/persistence/pgsql/repository/AuthUserPgsqlRepository.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/persistence/pgsql/AuthUserRepositoryAdapter.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/scheduler/TokenCleanupScheduler.java`
- `services/auth-service/src/test/java/com/socialseed/authservice/TokenCleanupIntegrationTest.java`
