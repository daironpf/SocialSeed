# Issue Summary: Credential Expiration Strategy (#71)

## Overview
Implemented a password expiration policy that automatically flags user credentials as expired after a configurable period (default 90 days).

## Platform Components Added
- **Error Code**: `PASSWORD_EXPIRED` (401 Unauthorized)
- **I18n Key**: `auth.error.password_expired`

## Service Logic
- Users are flagged by a daily background job (`PasswordExpirationScheduler`).
- Login is rejected for flagged users.
- A password change resets the expiration timer and flags the account as valid.

## Configuration
- `auth.password.expiration-days`: Number of days before password expires (default: 90).
- `auth.password.expiration-cron`: Schedule for the background job (default: `0 0 0 * * ?` - daily at midnight).

## Manual Testing
To test the password expiration flow:

1. **Register a user**: `POST /auth/register`
2. **Backdate the password change date** (Manual DB update):
   ```sql
   UPDATE auth_users SET last_password_changed_at = NOW() - INTERVAL '91 days' WHERE email = '...';
   ```
3. **Trigger the scheduler** (or wait for midnight).
4. **Try to login**: `POST /auth/login`. Expect `401 Unauthorized` with message "Your password has expired and must be changed."
5. **Change password**: `POST /auth/password/change`.
6. **Login again**: Should work successfully.
