# Issue #78: Remove Role from User (Admin) - Summary

## Overview
Implemented token invalidation functionality for the admin endpoint to remove roles from users. When a role is removed, all refresh tokens for the affected user are revoked to ensure immediate reflection of role changes in authorization checks, preventing unauthorized access with outdated tokens.

## Changes Made

### Domain Layer
- **AuthService.java**: Added `long countUsersWithRole(String role);` method to support validation logic.
- **RefreshTokenRepository.java**: Added `Optional<RefreshToken> findByToken(String token);` method for token lookup operations.

### Application Layer
- **RemoveRoleFromUser.java**: Added call to `authService.revokeAllTokensForUser(userId);` after role removal and user save to invalidate tokens immediately.

### Infrastructure Layer
- **AuthServiceImpl.java**: Implemented `countUsersWithRole` by delegating to `authUserRepository.countUsersWithRole(role);` and `revokeAllTokensForUser` by iterating through user tokens and revoking them.
- **RefreshTokenRepositoryAdapter.java**: Added implementation for `findAllByUserId(UUID userId)` to match domain interface.
- **RefreshTokenPgsqlRepository.java**: Added `Optional<RefreshTokenPgsqlEntity> findByToken(String token);` method for JPA-based token lookup.

### Test Layer
- **RemoveRoleFromUserTest.java**: Updated test expectations and added verifications for `revokeAllTokensForUser` calls in successful role removal scenarios. Fixed assertion for user not found case (changed to expect `USER_NOT_FOUND` instead of `ROLE_NOT_FOUND`).

## Platform Updates
No changes were made to platform starters (validation, error-handling, api-response, contracts) as existing centralized resources were sufficient.

## Security Impact
- **Enhanced Security**: Token invalidation ensures that role changes are reflected immediately in authorization, preventing users from retaining access with removed roles until token expiry.
- **No Breaking Changes**: Existing functionality remains intact; new behavior only activates on role removal.
- **Compliance**: Aligns with security best practices by forcing re-authentication after privilege changes.

## Testing Results
- **Unit Tests**: All 8 tests in `RemoveRoleFromUserTest` pass, including verifications for token revocation calls.
- **Integration Tests**: All auth-service integration tests (LoginIntegrationTest, ResetPasswordIntegrationTest, etc.) pass without regressions.
- **Coverage**: Unit tests cover business logic and token invalidation; integration tests verify end-to-end functionality.

## API Usage
### Endpoint: `DELETE /api/roles/{userId}`
- **Description**: Admin endpoint to remove a specific role from a user.
- **Parameters**:
  - `userId` (path): UUID of the user
  - Request Body: `{ "role": "ROLE_ADMIN" }` (example)
- **Headers**: `Authorization: Bearer <admin-jwt-token>`
- **Response**: `200 OK` with updated roles set, or error codes (USER_NOT_FOUND, ROLE_NOT_FOUND, CANNOT_REMOVE_LAST_ADMIN, CANNOT_REMOVE_OWN_USER_ROLE)
- **Security**: Requires ROLE_ADMIN; validates admin count to prevent removing last admin; invalidates user tokens on success.

### Example cURL
```bash
curl -X DELETE "http://localhost:8080/api/roles/123e4567-e89b-12d3-a456-426614174000" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{"role": "ROLE_ADMIN"}'
```

## Files Modified
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/domain/service/AuthService.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/domain/repository/RefreshTokenRepository.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/application/usecase/RemoveRoleFromUser.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/service/AuthServiceImpl.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/persistence/pgsql/repository/RefreshTokenPgsqlRepository.java`
- `services/auth-service/src/main/java/com/socialseed/authservice/auth/infrastructure/persistence/pgsql/RefreshTokenRepositoryAdapter.java`
- `services/auth-service/src/test/java/com/socialseed/authservice/auth/application/usecase/RemoveRoleFromUserTest.java`