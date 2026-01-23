# Summary: Cleanup Expired Password Reset Tokens (Issue #95)

## Overview
Implemented an automated cleanup mechanism for expired security tokens in the `auth-service`. This ensures that expired password reset tokens and email verification tokens are removed from the database periodically, improving security and reducing data stale-ness.

## Changes Implemented

### Domain & Persistence
- **Repository Interface**: Updated `AuthUserRepository` with methods for bulk clearing expired tokens.
- **JPA Implementation**: Added optimized `@Modifying` update queries to `AuthUserPgsqlRepository` to nullify expired tokens in a single database round-trip.

### Infrastructure
- **Scheduler**: Created `TokenCleanupScheduler` using Spring's `@Scheduled` annotation.
- **Configuration**: Enabled scheduling in `AuthServiceApplication`.
- **Customization**: Added `auth.cleanup.cron` property support (defaulting to every hour).

### Verification
- Created `TokenCleanupIntegrationTest` to validate that:
  - Expired tokens (Reset/Verification) are correctly removed.
  - Non-expired tokens are preserved.
  - Performance is efficient via bulk updates.

## Files Modified
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/domain/repository/AuthUserRepository.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/persistence/pgsql/repository/AuthUserPgsqlRepository.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/persistence/pgsql/AuthUserRepositoryAdapter.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/AuthServiceApplication.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/scheduler/TokenCleanupScheduler.java` (New)
- `services/auth-service/src/test/java/com/socialseed/authservice/TokenCleanupIntegrationTest.java` (New)
