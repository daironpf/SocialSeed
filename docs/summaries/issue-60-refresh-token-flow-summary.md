# Refresh Token Flow Implementation Summary - Issue #60

## Overview
This document summarizes the implementation of the Refresh Token rotation flow for the `auth-service`. The feature enhances security by issuing new refresh tokens whenever an access token is refreshed, and includes a reuse detection mechanism that revokes all user sessions if an old refresh token is reused.

## Changes Made

### Domain Layer
- **`RefreshToken` Domain Model**:
    - Added `rotated` boolean field to track if the token has already been used to generate new tokens.
    - Added `rotate()` method to mark the token as used.
    - Updated `isValid()` to include `!rotated` check.

### Infrastructure Layer
- **`RefreshTokenPgsqlEntity`**:
    - Added `rotated` boolean column to the `refresh_tokens` table.
- **`RefreshTokenMapper`**:
    - Updated to include the `rotated` field in bidirectional mapping between domain and persistence.

### Application Layer
- **`RefreshToken` Use Case**:
    - New use case to handle the token refresh process.
- **`AuthService` Updates**:
    - Added `refreshToken(String refreshToken)` method.
    - **`AuthServiceImpl` Implementation**:
        - **Rotation**: When a valid token is refreshed, it is marked as `rotated`.
        - **Reuse Detection**: If a token with `rotated = true` is used again, the service deletes all refresh tokens for that user (session revocation) and throws a security breach exception.
        - **Issuance**: Generates a new Access Token and a new Refresh Token.
- **`AuthUseCases` Facade**:
    - Integrated the `RefreshToken` use case.

## Platform Updates

### Validation Starter
- **`@ValidUUID`**: New validator added to `socialseed-validation-starter` to ensure refresh tokens follow the UUID format.

### Error Handling Starter
- **`PGSQLExceptionHandler`**: New specialized handler for PostgreSQL errors (e.g., uniqueness violations).
- **`ErrorCode`**: Added entries for `AUTH_REUSE_DETECTION`, `REFRESH_TOKEN_NOT_FOUND`, and `REFRESH_TOKEN_INVALID_EXPIRED`.

### API Response Starter
- **`messages.properties`**: Centralized all authentication and security breach messages.

### Entry Layer (REST)
- **`RefreshTokenRequestDTO`**:
    - New request record with `refreshToken` field.
- **`AuthController`**:
    - Added `POST /auth/token/refresh` endpoint.
    - Returns the new `AuthResponseDTO` (token, refreshToken, roles).

### Internationalization
- **`messages.properties`**:
    - Added `auth.token.refresh.success` for successful rotation.
    - Added `refresh.token.required` for validation errors.

### Testing
- **`RefreshTokenServiceTest`**:
    - `shouldRotateTokenSuccessfully`: Verifies tokens are correctly rotated and new ones issued.
    - `shouldDetectReuseAndRevokeAllSessions`: Verifies reuse detection triggers full session revocation.
    - `shouldFailIfTokenExpired`: Verifies expired tokens cannot be used.
    - `shouldFailIfTokenRevoked`: Verifies revoked tokens cannot be used.

## Security Impact
- **Refresh Token Rotation**: Each refresh cycle provides a completely new refresh token, limiting the window of opportunity if a token is stolen.
- **Defensive Revocation**: Attempting to use a previously rotated token is treated as a security breach, immediately invalidating all other active sessions for that user.

## API Usage

### Refresh Token Request
```bash
POST /auth/token/refresh
Content-Type: application/json

{
  "refreshToken": "your-current-refresh-token-uuid"
}
```

### Success Response
```json
{
  "status": 200,
  "message": "Token refreshed successfully",
  "data": {
    "token": "new-access-token-jwt",
    "refreshToken": "new-refresh-token-uuid",
    "roles": ["ROLE_USER"]
  }
}
```
