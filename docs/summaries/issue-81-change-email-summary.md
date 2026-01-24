# Issue Summary: Change Email (with Verification) (#81)

## Overview
Implemented a two-step process for changing a user's email address to ensure security and validity.

## Flow
1.  **Initiate**: User requests to change email to `newEmail`.
    - Endpoint: `POST /auth/change-email`
    - Body: `{ "newEmail": "string" }`
    - Action: Server validates uniqueness, generates a token, saves `newEmail` as `pendingEmail`, and sends a verification link.
    - Response: 200 OK "Verification email sent..."

2.  **Verify**: User clicks the link or submits the token.
    - Endpoint: `POST /auth/verify-email-change`
    - Body: `{ "token": "string" }`
    - Action: Server validates token expiry and content. If valid, `email` is updated to `pendingEmail`, and pending fields are cleared.
    - Response: 200 OK "Email changed successfully."

## Database Changes
- Added columns to `auth_users` table:
    - `pending_email` (VARCHAR)
    - `email_change_token` (VARCHAR)
    - `email_change_token_expiry` (TIMESTAMP)

## Architecture Updates
- **Platform**: Added `EMAIL_ALREADY_EXISTS` error code.
- **Repository**: Added `findByEmailChangeToken` method.
