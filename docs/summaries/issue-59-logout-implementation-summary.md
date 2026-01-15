# Logout Implementation Summary - Issue #59

## Overview
This document summarizes the complete implementation of the logout functionality for the `auth-service`. The feature allows users to securely log out by invalidating their refresh tokens and optionally blacklisting their access tokens in Redis.

## Changes Made

### Domain Layer
- **`RefreshToken` Domain Model**:
    - Implemented as a Java class with fields: `id`, `token`, `userId`, `expiryDate`, and `revoked`.
    - Factory method `create()` generates new refresh tokens with configurable expiration.
    - Business logic methods: `revoke()`, `isExpired()`, `isValid()`.

- **`TokenBlacklistService` Interface**:
    - Defines contract for blacklisting access tokens.
    - Methods: `blacklistToken(String jti, Duration expiration)` and `isBlacklisted(String jti)`.

- **`RefreshTokenRepository` Interface**:
    - Repository contract for refresh token persistence.
    - Methods: `save()`, `findByToken()`, `deleteByToken()`, `deleteByUserId()`.

### Infrastructure Layer
- **`RefreshTokenPgsqlEntity`**:
    - JPA entity for persisting refresh tokens in PostgreSQL.
    - Maps domain `RefreshToken` to database table `refresh_tokens`.

- **`RefreshTokenPgsqlRepository`**:
    - Spring Data JPA repository extending `JpaRepository`.

- **`RefreshTokenMapper`**:
    - Bidirectional mapper between domain and persistence entities.

- **`RefreshTokenRepositoryAdapter`**:
    - Adapter implementing `RefreshTokenRepository` using Spring Data repository.

- **`RedisTokenBlacklistService`**:
    - Redis-based implementation of `TokenBlacklistService`.
    - Uses `StringRedisTemplate` to store blacklisted JTIs with TTL.
    - Prefix: `token:blacklist:`.

### Application Layer
- **`Logout` Use Case**:
    - Orchestrates logout logic by calling `AuthService.logout()`.

- **`AuthService` Updates**:
    - Added `logout(String accessToken, String refreshToken)` method.
    - Implementation in `AuthServiceImpl`:
        - Invalidates refresh token by marking it as revoked.
        - Extracts JTI from access token and blacklists it in Redis with remaining TTL.

- **`AuthUseCases` Facade**:
    - Integrated `Logout` use case.
    - Added public method `logout(String accessToken, String refreshToken)`.

### Entry Layer (REST)
- **`LogoutRequestDTO`**:
    - Request record with `refreshToken` field.
    - Validation: `@NotBlank` on `refreshToken`.

- **`AuthController`**:
    - Added `POST /auth/logout` endpoint.
    - Accepts `Authorization` header (optional) and `LogoutRequestDTO` body.
    - Returns `204 No Content` on success.

### Security & Configuration
- **`JWTProvider` Updates**:
    - Added JTI (JWT ID) to token generation using `UUID.randomUUID()`.
    - New methods: `getJtiFromToken()` and `getExpirationDateFromToken()`.

- **`JwtAuthFilter` Updates**:
    - Injected `TokenBlacklistService`.
    - Checks if token JTI is blacklisted before authenticating requests.
    - Rejects blacklisted tokens.

- **`SecurityConfig` Updates**:
    - Injected `TokenBlacklistService` and passed to `JwtAuthFilter`.

- **`AuthResponseDTO` Updates**:
    - Added `refreshToken` field to return refresh tokens on login/register.

### Configuration
- **`application.yml`**:
    - Added Redis configuration: `spring.data.redis.host` and `spring.data.redis.port`.
    - Updated JWT configuration: `jwt.refresh.expiration` (30 days).

- **`docker-compose.yml`**:
    - Added Redis service with Alpine image (7.4-alpine).
    - Memory limit: 128MB with LRU eviction policy.
    - Health check using `redis-cli ping`.
    - Added `REDIS_HOST` and `REDIS_PORT` environment variables to `auth-service`.

- **`pom.xml`**:
    - Added `spring-boot-starter-data-redis` dependency.
    - Updated `maven-surefire-plugin` to version 3.2.5 for JUnit 5 support.

### Testing
- **Unit Tests**:
    - `LogoutUseCaseTest`: Verifies the `Logout` use case calls `AuthService.logout()`.
    - `AuthServiceImplLogoutTest`: Tests refresh token invalidation and access token blacklisting logic.
    - `AuthControllerTest`: Tests the `/auth/logout` endpoint returns 204 No Content.

- **Results**: All 4 tests passed successfully.

## Database Schema
A new table `refresh_tokens` will be created automatically by JPA with the following structure:
- `id` (UUID, primary key)
- `token` (VARCHAR, unique, not null)
- `user_id` (UUID, not null)
- `expiry_date` (TIMESTAMP, not null)
- `revoked` (BOOLEAN, not null)

## Redis Storage
Blacklisted tokens are stored in Redis with:
- Key pattern: `token:blacklist:{jti}`
- Value: `"true"`
- TTL: Remaining time until token expiration

## API Usage

### Logout Request
```bash
POST /auth/logout
Authorization: Bearer {access_token}
Content-Type: application/json

{
  "refreshToken": "valid-refresh-token-uuid"
}
```

### Response
```
HTTP/1.1 204 No Content
```

## Next Steps
- Consider implementing a cleanup job for expired refresh tokens in PostgreSQL.
- Add metrics/monitoring for logout operations.
- Implement "logout from all devices" functionality using `deleteByUserId()`.

## Issue Status
Issue #59 is now complete and ready for testing with Redis and PostgreSQL running.
