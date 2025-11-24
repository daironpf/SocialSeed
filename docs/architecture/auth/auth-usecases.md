
```
/docs/architecture/auth/auth-usecases.md
```

---

# 📄 **auth-usecases.md**

### *Auth-Service – Use Case Specification*

**Project:** SocialSeed
**Service:** Auth-Service
**Version:** 1.0
**Author:** SocialSeed Architecture Team

---

# **1. Overview**

The **Auth-Service** is responsible for authentication, authorization, identity lifecycle management, credential security, session and token issuing, and user security events.
It is entirely decoupled from SocialUser-Service, which handles social graph identity.
Auth-Service owns:

* login
* registration
* password management
* email verification
* security policies
* 2FA
* token lifecycle
* admin identity controls

Auth-Service communicates with SocialUser-Service via **gRPC** to register a corresponding social node.

---

# **2. Design Principles**

* **Hexagonal architecture** (application/usecase → domain → infrastructure)
* **Stateless API where possible**
* **JWT tokens** for access/refresh logic
* **Strong audit logging**
* **Multi-service boundary clarity**
* **Security first** (OWASP ASVS, NIST standards)
* **Event-driven integration** (Kafka for async domain events)

---

# **3. Use Cases List (Complete)**

This section lists *all* intended use cases for the Auth-Service.

---

# **A. Identity Lifecycle Management**

### **A1. Register User**

Create a new auth identity after verifying uniqueness and creating the social graph node via gRPC.
**Input:** username, email, password
**Output:** Auth user created + JWT token
**Events:** UserRegistered, SocialUserNodeCreated

---

### **A2. Get User by ID**

Retrieve auth-level user data.
**Input:** userId
**Output:** user details

---

### **A3. Get User by Email or Username**

Look up user identity for login or profile updates.
**Input:** email / username
**Output:** user details

---

### **A4. Sync User Data With SocialUser-Service**

Used internally when username/email are changed.
**Output:** success/failure

---

# **B. Authentication**

### **B1. Login User**

Validate credentials, generate tokens, update lastLoginAt and lastLoginIp.
**Input:** email, password
**Output:** JWT access + refresh token
**Events:** UserLoggedIn

---

### **B2. Logout User**

Invalidate refresh tokens.
**Input:** refresh token
**Output:** void

---

### **B3. Refresh Token**

Issue new access token if refresh token is valid.
**Input:** refresh token
**Output:** new access token

---

### **B4. Force Logout (Admin)**

Admin invalidates all tokens for a user.
**Input:** userId
**Events:** UserForcedLogout

---

# **C. Password Management**

### **C1. Change Password**

User provides current password + new password.
**Input:** userId, currentPassword, newPassword
**Events:** PasswordChanged

---

### **C2. Forgot Password – Generate Token**

Send email with password reset token.
**Input:** email
**Output:** token generated + expiration
**Events:** PasswordResetRequested

---

### **C3. Reset Password With Token**

Validate token, update password.
**Events:** PasswordResetCompleted

---

### **C4. Admin Reset Password**

Admin sets password without token.

---

# **D. Email Verification**

### **D1. Generate Email Verification Token**

After registration or email change.
**Events:** EmailVerificationRequested

---

### **D2. Verify Email**

Validate token and mark emailVerified = true.
**Events:** EmailVerified

---

### **D3. Resend Verification Email**

User requests new token.

---

# **E. Security Policies**

### **E1. Track Failed Login Attempts**

Increment counter on each wrong login.

---

### **E2. Automatic Account Lock**

If failedLoginAttempts exceed threshold.
**Events:** AccountLocked

---

### **E3. Unlock Account (User or Admin)**

Reset lock state.
**Events:** AccountUnlocked

---

### **E4. Credential Expiration Strategy**

Check if passwords require renewal after N days.

---

### **E5. Require Password Change on First Login**

Optional policy.

---

# **F. 2FA – Two-Factor Authentication**

### **F1. Enable 2FA**

Generate secret (TOTP) and require validation.
**Events:** TwoFactorEnabled

---

### **F2. Disable 2FA**

---

### **F3. Validate 2FA Token (During Login)**

---

### **F4. Regenerate Recovery Codes**

---

# **G. Roles & Permissions**

### **G1. Assign Role to User (Admin)**

**Events:** RoleAssigned

---

### **G2. Remove Role from User (Admin)**

**Events:** RoleRemoved

---

### **G3. List User Roles**

---

# **H. Account Settings**

### **H1. Change Username**

Must sync with SocialUser-Service.
**Events:** UsernameChanged

---

### **H2. Change Email**

Triggers email verification flow.

---

### **H3. Disable Account (Soft Delete)**

---

### **H4. Reactivate Account**

---

### **H5. Permanently Delete Account**

Requires admin confirmation.
**Events:** UserDeleted

---

# **I. Audit & Events**

### **I1. Record Login Attempt**

Success or failure.

---

### **I2. Record Token Issuance**

---

### **I3. Record Security Changes**

Passwords, 2FA, email, username…

---

### **I4. Produce Domain Events to Kafka**

Emit:

* UserRegistered
* PasswordChanged
* EmailVerified
* RoleAssigned
* UserDeleted
  etc.

---

# **J. Internal Integration (S2S)**

### **J1. Create SocialNode via gRPC**

Called during registration.

---

### **J2. Sync Username Change to SocialUser-Service**

---

### **J3. Sync Email Change to SocialUser-Service**

---

### **J4. Validate Remote User Link**

(Ensure IDs match across systems.)

---

# **K. Maintenance & DevOps Use Cases**

### **K1. Cleanup Expired Tokens**

Cron task.

---

### **K2. Cleanup Expired Email Verification Tokens**

---

### **K3. Cleanup Expired Password Reset Tokens**

---

### **K4. Export User Security Logs**

Admin-only.

---

### **K5. Health Check & Metrics**

---

# **4. Use Case Relationships (Architecture)**

* "RegisterUser" consumes **gRPC** → SocialUser-Service
* "LoginUser" triggers → Tracking & token issuance
* "ChangePassword" uses password encoder inside domain service
* "ResetPassword" requires email-service integration
* "VerifyEmail" updates domain + sends event
* "Enable2FA" interacts with TOTP generator
* Admin use cases use restricted role checks
* Security policies hook into login pipeline

---

# **5. Event Model**

### **Main events emitted via Kafka:**

| Event                        | Trigger            |
| ---------------------------- | ------------------ |
| `UserRegistered`             | register user      |
| `UserLoggedIn`               | login success      |
| `PasswordChanged`            | change password    |
| `PasswordResetRequested`     | forgot password    |
| `PasswordResetCompleted`     | reset password     |
| `EmailVerificationRequested` | email verification |
| `EmailVerified`              | verify email       |
| `UserForcedLogout`           | admin action       |
| `UserDeleted`                | delete account     |

These events allow SocialSeed to scale horizontally and integrate additional microservices (notification-service, fraud-service, analytics-service, etc.)

---

# **6. Definitions**

* **Identity:** User credentials and auth metadata
* **Session:** Logical authenticated state
* **Access token:** Short-lived JWT
* **Refresh token:** Long-lived re-authentication token
* **2FA:** Extra verification step using TOTP
* **Security event:** Any identity-related activity

---

# **7. Future Extensions**

* OAuth2 login with Google / Apple
* Device management (trusted devices)
* Session history UI
* Security intelligence (fraud detection)
* Multi-factor authentication (SMS, email, WebAuthn)
* Role-based permission graphs

---

# **Summary**

This document defines the **complete identity & authentication scope** for the Auth-Service.
It ensures the service is fully isolated, scalable, and professionally designed following modern software architecture patterns.
