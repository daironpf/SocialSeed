# Issue Summary: Email Verification Flow Implementation

## Description
Implementation of the email verification flow for the `auth-service`. This ensures users must verify their identity via email token after registration.

## Platform Components Integrated
The implementation strictly followed SocialSeed's centralized platform standards:

- **Error Codes**: Reused `VERIFICATION_TOKEN_INVALID`, `VERIFICATION_TOKEN_EXPIRED`, and `EMAIL_ALREADY_VERIFIED` from `socialseed-error-handling-starter`.
- **i18n Messages**: Utilized `auth.verify.email.success`, `auth.resend.verification.success`, and related keys in `socialseed-api-response-starter`.
- **Validation**: Updated `ResendVerificationEmailRequestDTO` to use standard Jakarta `@Email` validation.

## Service Changes (Hexagonal Architecture)

### Domain Layer
- Added `verifyEmail` and `resendVerificationEmail` method signatures to `AuthService`.
- Reused `AuthUserRepository`'s existing `findByVerificationToken` capability.

### Application Layer
- Integrated `VerifyEmail` and `ResendVerificationEmail` use cases.
- Updated `AuthUseCases` facade to expose these functionalities.

### Infrastructure Layer
- **AuthServiceImpl**: Implemented token validation logic, including single-use semantics (token is cleared after success) and 24-hour expiry check.
- **StubEmailService**: Verified and used for local testing to simulate sending verification emails.

### Entry Point (REST)
- Exposed `POST /auth/verify-email`
- Exposed `POST /auth/resend-verification`

## Verification

### Automated Testing
- **Integration Tests**: Created `VerifyEmailIntegrationTest` and `ResendVerificationEmailIntegrationTest` covering success, invalid tokens, expired tokens, and duplicate verification.
- **E2E Flow**: Updated `verify_services/verify_auth_flow.py` to include regression testing for the new endpoints.

### Manual Testing Guide

#### 1. Register a new user
```bash
curl -X POST http://localhost:8081/auth/register \
     -H "Content-Type: application/json" \
     -d '{       
       "username": "testuser_verif",
       "email": "test_verif@example.com",
       "password": "StrongPass1!"
     }'
```
*Check logs for the verification token generated in the StubEmailService output.*

#### 2. Resend Verification Email
```bash
curl -X POST http://localhost:8081/auth/resend-verification \
     -H "Content-Type: application/json" \
     -d '{
       "email": "test_verif@example.com"
     }'
```

#### 3. Verify Email
```bash
curl -X POST http://localhost:8081/auth/verify-email \
     -H "Content-Type: application/json" \
     -d '{
       "token": "YOUR_TOKEN_FROM_LOGS"
     }'
```

#### 4. Verify Email (Negative - Inexistent Token)
```bash
curl -X POST http://localhost:8081/auth/verify-email \
     -H "Content-Type: application/json" \
     -d '{
       "token": "invalid-token-123"
     }'
```
*Expected: 400 Bad Request with "Verification token is invalid"*
