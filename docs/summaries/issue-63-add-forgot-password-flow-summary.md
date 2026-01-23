# Issue 63: Add Forgot Password & Reset Password Flow - Summary

## Overview
Implemented a secure forgot password and reset password flow for the Auth Service. This involves token generation, email notification (stubbed), and password update with token validation.

## Architectural Changes

### Service Layer (`auth-service`)
- **New Use Cases**:
  - `ForgotPassword`: Handles token generation and email dispatch.
  - `ResetPassword`: Handles token validation and password reset.
- **Persistence**:
  - Updated `AuthUserPgsqlEntity` to support `resetPasswordToken` and expiry.
  - Updated `AuthUserRepository` and `AuthUserPgsqlRepository` to find users by reset token.
- **Events**:
  - `PasswordResetRequestedEvent`
  - `PasswordResetCompletedEvent`

### Platform Layer (`socialseed-platform`)
- **Error Handling (`socialseed-error-handling-starter`)**:
  - Added new `ErrorCode` values:
    - `RESET_TOKEN_INVALID` (400 Bad Request)
    - `RESET_TOKEN_EXPIRED` (400 Bad Request)
- **Internationalization (`socialseed-api-response-starter`)**:
  - Added messages:
    - `auth.forgot.password.success`
    - `auth.reset.password.success`
    - `auth.error.reset_token_invalid`
    - `auth.error.reset_token_expired`

## Deviation & Resolution
- **Message Organization**: Initially added messages to local `auth-service/src/main/resources/messages.properties`. This violated the "No local messages.properties" constraint.
- **Correction**: Moved all auth-service messages to `socialseed-api-response-starter` and deleted the local properties file.

## Verification
- **Integration Tests**: Added `ForgotPasswordIntegrationTest` and `ResetPasswordIntegrationTest` covering positive and negative scenarios.
- **Manual Verification**: Verified via local environment logs stubbed email sending.

## Manual Testing

### Forgot Password
**Endpoint**: `POST http://localhost:8081/auth/forgot-password`

**Body** (JSON):
```json
{
  "email": "user@example.com"
}
```

**Expected Response**: `200 OK`
```json
{
  "data": null,
  "message": "If an account exists for that email, we have sent a password reset link",
  "status": 200
}
```

**Note**: Check application logs for the generated token.

### Reset Password
**Endpoint**: `POST http://localhost:8081/auth/reset-password`

**Body** (JSON):
```json
{
  "token": "TOKEN_FROM_LOGS",
  "newPassword": "NewSecurePassword123!"
}
```

**Expected Response**: `200 OK`
```json
{
  "data": null,
  "message": "Password successfully reset",
  "status": 200
}
```

**Error Cases**:
- Invalid token: `400 Bad Request` - "Invalid password reset token"
- Expired token: `400 Bad Request` - "Password reset token has expired"

