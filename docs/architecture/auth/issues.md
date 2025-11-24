
---

## EPIC: Identity Lifecycle

### Issue: Implement User Registration Flow

**Priority:** High
**Labels:** `feature`, `architecture`, `security`, `persistence`

**Description:**
Implement the complete registration workflow in auth-service. Workflow includes: request validation, email/username uniqueness checks, password hashing, persist user in Postgres using the provided `AuthUserPgsqlEntity` schema, call SocialUser-Service via gRPC to create the social node, handle failure/compensation, and emit a `UserRegistered` domain event (to Kafka).

**Acceptance Criteria:**

* POST `/auth/register` accepts `username`, `email`, `password` and returns 201 on success.
* Email and username uniqueness validated at domain and DB level; conflict returns structured error.
* Password stored hashed (BCrypt/Argon2) — plain text never persisted or logged.
* A gRPC call to socialuser-service is made to create social node and returned `userId` is used as primary id.
* If socialuser creation fails, the registration either rolls back or compensates (behavior documented).
* `UserRegistered` event is produced to Kafka/outbox with consistent schema.
* Response conforms to SURE envelope (success, data, meta).
* Unit and integration tests cover happy path and failure scenarios.

---

### Issue: Implement Get User by ID

**Priority:** Medium
**Labels:** `feature`, `rest`, `persistence`

**Description:**
Create endpoint & use-case to retrieve user details (excluding password) by UUID. This endpoint is primarily for internal services or admin usage; must respect RBAC.

**Acceptance Criteria:**

* GET `/auth/users/{id}` returns 200 with user data excluding password.
* Returns 404 if user not found.
* Includes metadata (createdAt, emailVerified, roles) in response.
* Proper authorization: only internal services or admin role can access (configurable).
* Covered by unit tests and integration tests.

---

### Issue: Implement Get User by Email or Username

**Priority:** Medium
**Labels:** `feature`, `rest`, `persistence`

**Description:**
Add lookup endpoints and use-cases for fetching user by email or username to support login and internal verification flows. Ensure normalization (lowercase emails) and rate-limit to prevent enumeration.

**Acceptance Criteria:**

* Endpoint(s) exist: `GET /auth/users?email=...` and `GET /auth/users?username=...` (internal use).
* Returns 200 with user info excluding password, or 404.
* Rate-limiting applied and documented.
* Protected for internal calls or via API key.
* Tests validate normalization and behavior.

---

### Issue: Sync User Data With SocialUser-Service (username/email changes)

**Priority:** Low
**Labels:** `feature`, `grpc`, `architecture`

**Description:**
When username or email changes in auth, call socialuser-service (gRPC) or emit event to ensure social node read-copies are updated. Implement retries and idempotency.

**Acceptance Criteria:**

* On username/email update, a gRPC call or Kafka event is issued.
* Retry policy implemented (exponential backoff) on failures.
* Consumer on socialuser side performs idempotent update.
* Failures are logged and put on DLQ for manual inspection.
* Unit tests and integration tests for retry behaviour.

---

## EPIC: Authentication

### Issue: Implement User Login and Token Generation

**Priority:** Critical
**Labels:** `feature`, `security`, `architecture`

**Description:**
Implement secure login flows (email/username + password). On success, issue RS256-signed JWT access token and an opaque refresh token stored in DB. Track login metadata and failed attempts.

**Acceptance Criteria:**

* POST `/auth/login` accepts credentials and returns SURE envelope with access + refresh token.
* Password verification uses secure password service adapter.
* On success: update lastLoginAt and lastLoginIp, reset failedLoginAttempts.
* On failure: increment failedLoginAttempts, update lastFailedLoginAt/IP.
* 2FA step included in pipeline (skipped if not enabled for user).
* Token payload includes `sub` (userId), `roles`, `iat`, `exp`, `jti`.
* Tests cover success, invalid credentials, locked accounts.

---

### Issue: Implement Logout

**Priority:** Medium
**Labels:** `feature`, `security`

**Description:**
Implement logout flow to invalidate refresh tokens and optionally add short-lived access tokens to a blacklist store (Redis) if immediate revocation is required.

**Acceptance Criteria:**

* POST `/auth/logout` invalidates provided refresh token.
* If configured, access token jti is recorded in Redis blacklist until expiry.
* Response returns 204 (no content) on success.
* Tests for token invalidation and blacklist logic.

---

### Issue: Implement Refresh Token Flow

**Priority:** High
**Labels:** `feature`, `security`, `persistence`

**Description:**
Implement refresh endpoint to rotate refresh tokens and issue new access tokens. Detect token reuse and perform defensive actions (revoke all user sessions if suspicious reuse detected).

**Acceptance Criteria:**

* POST `/auth/token/refresh` accepts refresh token and issues new access + refresh tokens.
* Old refresh token invalidated/rotated.
* Token reuse detection triggers full session revocation and alerting.
* Refresh tokens persisted securely with expiry and used/rotated flags.
* Integration tests for rotation, reuse detection, and expiry.

---

### Issue: Implement Admin Forced Logout

**Priority:** Medium
**Labels:** `feature`, `security`, `admin` *(use `feature` + `security`; `admin` label not in list — keep `feature` & `security`)*

**Description:**
Provide admin capability to invalidate all refresh tokens for a user and force session termination across devices.

**Acceptance Criteria:**

* Admin endpoint `POST /admin/users/{id}/force-logout` invalidates all refresh tokens for user.
* Emits `UserForcedLogout` event.
* Audit log entry created with admin id and timestamp.
* Only admin roles can access; tests validate authorization.

---

## EPIC: Password Management

### Issue: Add Password Change Use Case

**Priority:** High
**Labels:** `feature`, `security`

**Description:**
Allow authenticated users to change password given current password verification. After change, revoke refresh tokens (or rotate) and emit `PasswordChanged` event.

**Acceptance Criteria:**

* Endpoint `POST /auth/{id}/change-password` requires authentication and current password.
* New password validated by policy (min length, complexity).
* Password stored hashed; previous tokens invalidated.
* Emit `PasswordChanged` event.
* Tests for correct verification and token invalidation.

---

### Issue: Add Forgot Password & Reset Password Flow

**Priority:** High
**Labels:** `feature`, `security`, `rest`

**Description:**
Implement forgot-password flow: generate secure token, send via email, validate token on reset, and change password securely.

**Acceptance Criteria:**

* POST `/auth/forgot-password` generates a one-time token with expiry and triggers email service (stubbed in tests).
* POST `/auth/reset-password` validates token and completes reset.
* Token single-use and expired tokens rejected.
* Emit `PasswordResetRequested` and `PasswordResetCompleted` events.
* Tests for token lifecycle and email integration.

---

### Issue: Implement Admin Reset Password

**Priority:** Low
**Labels:** `feature`, `security`

**Description:**
Allow admins to set user passwords directly (for account recovery scenarios) with audit logging.

**Acceptance Criteria:**

* Admin endpoint available and protected by role.
* Password is hashed and stored.
* Audit log records action and admin id.
* Tests for admin auth and action auditing.

---

## EPIC: Email Verification

### Issue: Implement Email Verification Token Generation

**Priority:** Medium
**Labels:** `feature`, `security`

**Description:**
Generate verification tokens on registration or email change, with expiry, and integrate with email delivery.

**Acceptance Criteria:**

* Token generation endpoint/logic exists.
* Tokens stored with expiry and single-use semantics.
* Email send triggered (plumbed to notification service).
* Tests for generation and storage.

---

### Issue: Implement Verify Email Workflow

**Priority:** Medium
**Labels:** `feature`, `security`

**Description:**
Endpoint to verify email tokens, mark user `emailVerified=true` and emit `EmailVerified` event.

**Acceptance Criteria:**

* `GET /auth/verify?token=...` validates token and updates user record.
* Expired/invalid tokens return standardized error codes.
* Event emitted and audit logged.
* Tests for happy path and invalid/expired token.

---

### Issue: Implement Resend Verification Email

**Priority:** Low
**Labels:** `enhancement`, `rest`, `security`

**Description:**
Allow users to request resending verification token; rate-limited to prevent abuse.

**Acceptance Criteria:**

* Endpoint available to request resend.
* Rate limit enforced per user/IP.
* New token invalidates previous token.
* Tests for rate limiting.

---

## EPIC: Security Policies

### Issue: Track Failed Login Attempts

**Priority:** High
**Labels:** `feature`, `security`, `persistence`

**Description:**
Implement tracking of failed login attempts with timestamp and IP for brute-force mitigation and reporting.

**Acceptance Criteria:**

* Failed login attempts increment counter on user entity and record lastFailedLoginAt and IP.
* Counter persisted in DB.
* Tests for counter increment and storage.

---

### Issue: Implement Automatic Account Locking

**Priority:** High
**Labels:** `feature`, `security`

**Description:**
Lock account after configurable threshold of failed attempts, send notification and allow admin override or unlock flow.

**Acceptance Criteria:**

* After N failed attempts (configurable), accountNonLocked set to false.
* Locked account returns standardized error response for login attempts.
* Admin endpoint or automated email flow to unlock provided.
* Tests for lock/unlock behavior.

---

### Issue: Unlock Account (User/Admin)

**Priority:** Low
**Labels:** `feature`, `security`

**Description:**
Provide means for users (email unlock) or admins to unlock accounts.

**Acceptance Criteria:**

* Unlock endpoint works and is protected.
* Admin audit log records unlock action.
* Tests included.

---

### Issue: Credential Expiration Strategy

**Priority:** Low
**Labels:** `enhancement`, `security`

**Description:**
Policy to enforce password expiration after configurable period and notify users.

**Acceptance Criteria:**

* Background job identifies expired passwords and flags users.
* Endpoints require password change if flagged.
* Tests for detection and flagging logic.

---

### Issue: Require Password Change on First Login (Optional)

**Priority:** Low
**Labels:** `enhancement`, `security`

**Description:**
If flagged, force user to change password on first login flow.

**Acceptance Criteria:**

* Login flow detects `mustChangePassword` flag and returns appropriate code.
* Change password completes and clears flag.
* Tests for flag flow.

---

## EPIC: Two-Factor Authentication (2FA)

### Issue: Enable 2FA (TOTP)

**Priority:** High
**Labels:** `feature`, `security`

**Description:**
Implement TOTP-based 2FA enrollment (secret generation, QR code), verification, and enablement.

**Acceptance Criteria:**

* Endpoint to start enrollment returns TOTP secret and QR data.
* Verification endpoint validates code and enables 2FA.
* Recovery codes created and returned securely.
* Tests for generation and validation.

---

### Issue: Disable 2FA

**Priority:** Medium
**Labels:** `feature`, `security`

**Description:**
Allow users to disable 2FA after verifying with TOTP or recovery code.

**Acceptance Criteria:**

* Disable endpoint requires verification and logs action.
* Tests for disable flow and security.

---

### Issue: Validate 2FA Token (During Login)

**Priority:** High
**Labels:** `feature`, `security`

**Description:**
Integrate 2FA into login flow: after password validation, require TOTP code if enabled.

**Acceptance Criteria:**

* Login pipeline supports 2-step flow.
* Proper session handling until 2FA validated.
* Tests for success and invalid codes.

---

### Issue: Generate Recovery Codes

**Priority:** Medium
**Labels:** `feature`, `security`

**Description:**
Generate one-time recovery codes for users to regain access if they lose TOTP device.

**Acceptance Criteria:**

* On generation, codes stored hashed and shown once to user.
* Using a code invalidates it.
* Tests included.

---

## EPIC: Roles & Permissions

### Issue: Assign Role to User (Admin)

**Priority:** Medium
**Labels:** `feature`, `security`

**Description:**
Admin UI/API to assign roles to users and emit a `RoleAssigned` event.

**Acceptance Criteria:**

* Protected admin endpoint to assign roles.
* Role changes validated (no unknown roles).
* Event emitted and audit logged.
* Tests for role assignment and permission boundaries.

---

### Issue: Remove Role from User (Admin)

**Priority:** Medium
**Labels:** `feature`, `security`

**Description:**
Admin endpoint to remove roles and reflect immediately in authorization checks.

**Acceptance Criteria:**

* Roles removed and persisted.
* Event emitted and audit logged.
* Tests for removal and security checks.

---

### Issue: List User Roles

**Priority:** Low
**Labels:** `feature`

**Description:**
Endpoint to list user roles for admin and internal services.

**Acceptance Criteria:**

* Endpoint returns roles set for a user.
* Protected properly.
* Tests included.

---

## EPIC: Account Settings

### Issue: Change Username (with Social Sync)

**Priority:** Medium
**Labels:** `feature`, `grpc`, `security`, `persistence`

**Description:**
Allow users to change username; validate uniqueness; sync change with socialuser-service via gRPC or event.

**Acceptance Criteria:**

* Endpoint validates uniqueness and updates DB.
* Sync occurs (gRPC call or event) and result handled idempotently.
* Emit `UsernameChanged` event.
* Tests for sync and uniqueness.

---

### Issue: Change Email (with Verification)

**Priority:** Medium
**Labels:** `feature`, `security`

**Description:**
Allow changing email, trigger verification flow, and block certain operations until verified.

**Acceptance Criteria:**

* On change, old email remains until new verified (or policy defined).
* Verification token issued and email sent.
* Tests for change and verification.

---

### Issue: Disable Account (Soft Delete)

**Priority:** Medium
**Labels:** `feature`, `persistence`

**Description:**
Mark account as disabled without removing data; stop login and produce audit event.

**Acceptance Criteria:**

* Account disabled toggles `enabled=false`.
* Prevent login and set appropriate response code.
* Admin API to re-enable.
* Tests included.

---

### Issue: Reactivate Account

**Priority:** Low
**Labels:** `feature`, `security`

**Description:**
Allow reactivation via admin or verified flows.

**Acceptance Criteria:**

* Reactivation endpoint works and logs action.
* Tests included.

---

### Issue: Permanently Delete Account

**Priority:** High
**Labels:** `feature`, `persistence`, `security`

**Description:**
Admin-only permanent deletion (GDPR/Right to be forgotten) with confirmation and audit trail.

**Acceptance Criteria:**

* Endpoint requires confirmation and admin role.
* Delete cascades or flags data as per policy.
* Emit `UserDeleted` event.
* Tests for safe deletion and audit.

---

## EPIC: Audit & Domain Events

### Issue: Implement Login Attempt Logging

**Priority:** Medium
**Labels:** `feature`, `persistence`, `security`

**Description:**
Log each login attempt (success/failure) into audit store and produce events for analytics.

**Acceptance Criteria:**

* Logs include userId/email (if provided), IP, timestamp, outcome, reason.
* Logs available to admin with pagination.
* Tests for log creation.

---

### Issue: Implement Token Issuance Logging

**Priority:** Medium
**Labels:** `feature`, `persistence`

**Description:**
Record issuance of access/refresh tokens for auditing and incident response.

**Acceptance Criteria:**

* Create records for token issuance with jti, userId, issuedAt, expiry.
* Retention policy configurable.
* Tests included.

---

### Issue: Implement Security Change Logging

**Priority:** Medium
**Labels:** `feature`, `persistence`, `security`

**Description:**
Log changes like password updates, 2FA toggles, role changes.

**Acceptance Criteria:**

* Logs stored in audit with actor, timestamp, and details (no sensitive data).
* Tests included.

---

### Issue: Produce Domain Events to Kafka

**Priority:** High
**Labels:** `feature`, `architecture`, `devops`

**Description:**
Implement outbox/publisher to reliably produce domain events to Kafka (UserRegistered, UsernameChanged, etc.). Ensure atomicity between DB writes and event emission.

**Acceptance Criteria:**

* Outbox table or transactional producer implemented.
* Events serialized with versioned schema and published reliably.
* DLQ handling for failed events.
* Integration tests for producer and consumer flows.

---

## EPIC: Internal Integration (S2S)

### Issue: Create SocialNode via gRPC

**Priority:** High
**Labels:** `grpc`, `feature`, `integration`

**Description:**
On registration, call socialuser-service gRPC `CreateSocialUser` method and use returned id as primary key.

**Acceptance Criteria:**

* gRPC stub configured and tested.
* Proper timeout and retry policies in client.
* Idempotency ensured (retries won't create duplicates).
* Tests for success/failure handling.

---

### Issue: Sync Username Change to SocialUser-Service

**Priority:** Medium
**Labels:** `grpc`, `feature`

**Description:**
On username change, notify socialuser-service via gRPC or event.

**Acceptance Criteria:**

* Sync mechanism in place with retry and idempotency.
* Event emitted if using Kafka.
* Tests included.

---

### Issue: Sync Email Change to SocialUser-Service

**Priority:** Medium
**Labels:** `grpc`, `feature`

**Description:**
Sync email change similarly; social nodes keep read-only copies.

**Acceptance Criteria:**

* Sync performed and idempotent.
* Tests included.

---

### Issue: Validate Remote User Link

**Priority:** Low
**Labels:** `feature`, `integration`

**Description:**
Check consistency between auth and socialuser records; tool to validate and reconcile divergence.

**Acceptance Criteria:**

* Small reconciliation job implemented to detect mismatches.
* Reports generated for manual fix.
* Tests for detection.

---

## EPIC: Maintenance & DevOps

### Issue: Cleanup Expired Tokens Job

**Priority:** Low
**Labels:** `task`, `devops`

**Description:**
Background job to remove expired refresh tokens and optionally blacklisted access tokens.

**Acceptance Criteria:**

* Cron job implemented and scheduled.
* Logs job run and deletion statistics.
* Tests for deletion logic.

---

### Issue: Cleanup Expired Email Verification Tokens

**Priority:** Low
**Labels:** `task`, `devops`

**Description:**
Remove expired verification tokens from DB.

**Acceptance Criteria:**

* Job scheduled and tested.

---

### Issue: Cleanup Expired Password Reset Tokens

**Priority:** Low
**Labels:** `task`, `devops`

**Description:**
Remove expired reset tokens.

**Acceptance Criteria:**

* Job scheduled and tested.

---

### Issue: Export Audit/Security Logs for Admin

**Priority:** Low
**Labels:** `feature`, `docs`

**Description:**
Admin capability to export logs for compliance review.

**Acceptance Criteria:**

* Admin endpoint to export logs in CSV/JSON.
* Access restricted to admin role.
* Docs updated on usage.

---

### Issue: Health Check & Metrics Endpoints

**Priority:** Medium
**Labels:** `feature`, `devops`

**Description:**
Expose actuator/health and Prometheus metrics endpoints. Include DB, Kafka, and gRPC health checks.

**Acceptance Criteria:**

* `/actuator/health` shows component states.
* Prometheus metrics exported.
* Tests or smoke checks run in CI.

---

## EPIC: Testing, Refactor & Docs

### Issue: Add Unit Tests for Domain & Use Cases

**Priority:** High
**Labels:** `testing`, `qa`

**Description:**
Create thorough unit tests for domain services, validators, and use-cases.

**Acceptance Criteria:**

* Coverage for critical use-cases (register, login, change password).
* CI enforces minimum coverage threshold (configurable).
* Tests run reliably in CI.

---

### Issue: Add Integration Tests for REST & gRPC Layers

**Priority:** High
**Labels:** `testing`, `qa`, `grpc`, `rest`

**Description:**
Integration tests using Testcontainers for Postgres, Kafka, Neo4j (or mocked), and gRPC.

**Acceptance Criteria:**

* End-to-end flows tested: register (including gRPC), login, refresh, reset password.
* Tests stable and part of CI pipeline.

---

### Issue: Refactor Auth Domain Models

**Priority:** Medium
**Labels:** `refactor`, `architecture`

**Description:**
Clean domain model, remove framework leakage into domain, ensure hexagonal boundaries.

**Acceptance Criteria:**

* Domain layer contains no Spring annotations.
* Infrastructure layer implements adapters only.
* Tests updated to reflect changes.

---

### Issue: Refactor Password Encryption Handling

**Priority:** Medium
**Labels:** `refactor`, `security`, `architecture`

**Description:**
Ensure password hashing and verification are contained in infrastructure adapter (PasswordService) and injected into use-cases.

**Acceptance Criteria:**

* Password encoding is abstracted behind interface.
* All code uses the adapter, no direct BCrypt references in use-cases.
* Tests prove behavior.

---

### Issue: Improve Project Documentation

**Priority:** Medium
**Labels:** `docs`, `task`

**Description:**
Add architecture overview, use-case documentation (auth-usecases.md), API endpoints doc, and contributor guidelines.

**Acceptance Criteria:**

* `/docs/architecture/auth/auth-usecases.md` present and up-to-date.
* README includes setup & run instructions.
* CONTRIBUTING.md added with PR & issue templates.

---

## Extra: Cross-cutting / Governance

### Issue: Create Kafka Topics and Schemas for Auth Events

**Priority:** High
**Labels:** `devops`, `architecture`, `dependencies`

**Description:**
Define topic names, partitions, retention, and Avro/Protobuf schema for events produced by Auth.

**Acceptance Criteria:**

* Topics created in dev with proper configs.
* Schemas stored in schema-registry or proto repo.
* Consumers and producers validated.

---

### Issue: Implement Outbox Pattern for Reliable Eventing

**Priority:** High
**Labels:** `architecture`, `devops`, `persistence`

**Description:**
Ensure atomicity between DB writes and events using outbox pattern (DB table + publisher).

**Acceptance Criteria:**

* Outbox table and background publisher implemented.
* Events marked as sent and audited.
* Integration tests show DB write + event publish atomicity.

---

### Issue: Add Rate Limiting and RequestId Middleware in API Gateway

**Priority:** High
**Labels:** `devops`, `architecture`

**Description:**
Ensure API Gateway adds `requestId`, enforces rate limiting, and forwards Accept-Language header.

**Acceptance Criteria:**

* Gateway injects unique requestId header for each request.
* Rate limiting rules configurable per route.
* Tests or smoke checks show header present.

---

### Issue: Implement JWKS Endpoint and Key Management Strategy

**Priority:** High
**Labels:** `security`, `devops`

**Description:**
Expose `/.well-known/jwks.json` and define key rotation strategy (KMS/Vault).

**Acceptance Criteria:**

* JWKS endpoint returns current public keys.
* Key rotation documented and testable (dev flow).
* Private keys not stored in repo/images.
* Tests for token verification against JWKS.

---

### Issue: Implement Structured Logging & Tracing Integration (OTel)

**Priority:** High
**Labels:** `devops`, `architecture`, `testing`

**Description:**
Add structured JSON logs and OpenTelemetry tracing; ensure trace propagation across REST/gRPC/Kafka.

**Acceptance Criteria:**

* Logs include requestId, userId, traceId, service/version.
* Traces appear in Jaeger for sample flows.
* Tests/integration show trace propagation.

---

### Issue: Create PR & Issue Templates + Labels Setup Script

**Priority:** Low
**Labels:** `docs`, `task`

**Description:**
Add GitHub templates for PRs and issues and script to create the label set.

**Acceptance Criteria:**

* `PULL_REQUEST_TEMPLATE.md` and `ISSUE_TEMPLATE.md` present.
* `labels.json` or script available to create labels using `gh` CLI.
* Documentation explains label usage.

---


