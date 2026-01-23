# Issue Summary: Track Failed Login Attempts (#68)

## Overview
Implemented a mechanism to track failed login attempts, recording the IP address and timestamp for each failure, and locking the account after 5 failed attempts.

## Changes Made
### Domain & Application
- Updated `LoginAttemptService` and `AuthService` to include IP address in the login flow.
- Updated `AuthenticateUser` use case and `AuthUseCases` facade to propagate the IP address.

### Infrastructure
- `LoginAttemptServiceImpl`: Persists `lastFailedLoginIp` and `lastFailedLoginAt`. Locks `accountNonLocked` when `failedLoginAttempts >= 5`.
- `AuthServiceImpl`: Captures IP and orchestrates success/failure recording.

### Entry (REST)
- `AuthController`: Extracts IP from `HttpServletRequest`.

## Platform Updates
- No new validators or error handlers were needed as existing ones were sufficient.

## Security Impact
- Provides brute-force protection via account locking.
- Enhanced auditing with IP tracking.

## Testing Results
- **Integration Tests**: `LoginTrackingIntegrationTest` passed (3 tests).
- **Controller Tests**: `AuthControllerTest` passed (2 tests).

## API Usage
- **Endpoint**: `POST /auth/login`
- **Request Body**: `{ "email": "...", "password": "..." }`
- **Headers**: The client's IP is automatically captured from the connection.
- **Behavior**: Returns `401 Unauthorized` on failure (message: `auth.error.invalid_credentials`). Returns `403 Forbidden` after 5 failures (message: `auth.error.account_locked`).
