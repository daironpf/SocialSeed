"""Data schema and DTOs for auth-service.

⚠️  IMPORTANT: This file uses camelCase aliases for compatibility with Java backends.
All fields with compound names must use alias="camelCaseName".

Example:
    refresh_token: str = Field(
        ...,
        alias="refreshToken",
        serialization_alias="refreshToken"
    )
"""

from typing import Optional, List
from pydantic import BaseModel, Field, EmailStr


# =============================================================================
# Endpoint Constants
# =============================================================================

ENDPOINTS = {
    # Auth Controller
    "register": "/auth/register",
    "login": "/auth/login",
    "logout": "/auth/logout",
    "refresh_token": "/auth/token/refresh",
    "forgot_password": "/auth/forgot-password",
    "reset_password": "/auth/reset-password",
    "verify_email": "/auth/verify-email",
    "verify_email_get": "/auth/verify",
    "resend_verification": "/auth/resend-verification",
    "change_password": "/auth/{id}/change-password",
    "change_email": "/auth/change-email",
    "verify_email_change": "/auth/verify-email-change",
    "change_username": "/auth/username",
    # User queries
    "get_user_by_id": "/auth/getUserById/{id}",
    "get_user_by_email": "/auth/getUserByEmail/{email}",
    "get_user_by_username": "/auth/getUserByUserName/{username}",
    # Role management
    "get_user_roles": "/auth/roles/user/{id}",
    "assign_role": "/auth/roles/assign",
    "remove_role": "/auth/roles/remove",
}


# =============================================================================
# Request Models
# =============================================================================


class RegisterRequestDTO(BaseModel):
    """DTO for user registration."""

    model_config = {"populate_by_name": True}

    username: str = Field(..., min_length=3, max_length=50)
    email: EmailStr
    password: str = Field(..., min_length=6)


class LoginRequestDTO(BaseModel):
    """DTO for user login."""

    model_config = {"populate_by_name": True}

    email: EmailStr
    password: str


class LogoutRequestDTO(BaseModel):
    """DTO for user logout."""

    model_config = {"populate_by_name": True}

    refresh_token: Optional[str] = Field(None, alias="refreshToken")


class RefreshTokenRequestDTO(BaseModel):
    """DTO for token refresh."""

    model_config = {"populate_by_name": True}

    refresh_token: str = Field(..., alias="refreshToken")


class ChangePasswordRequestDTO(BaseModel):
    """DTO for changing password."""

    model_config = {"populate_by_name": True}

    current_password: str = Field(..., alias="currentPassword")
    new_password: str = Field(..., alias="newPassword", min_length=6)


class ForgotPasswordRequestDTO(BaseModel):
    """DTO for forgot password."""

    model_config = {"populate_by_name": True}

    email: EmailStr


class ResetPasswordRequestDTO(BaseModel):
    """DTO for reset password."""

    model_config = {"populate_by_name": True}

    token: str
    new_password: str = Field(..., alias="newPassword", min_length=6)


class VerifyEmailRequestDTO(BaseModel):
    """DTO for email verification."""

    model_config = {"populate_by_name": True}

    token: str


class ResendVerificationRequestDTO(BaseModel):
    """DTO for resending verification email."""

    model_config = {"populate_by_name": True}

    email: EmailStr


class ChangeEmailRequestDTO(BaseModel):
    """DTO for changing email."""

    model_config = {"populate_by_name": True}

    new_email: EmailStr = Field(..., alias="newEmail")


class VerifyEmailChangeRequestDTO(BaseModel):
    """DTO for verifying email change."""

    model_config = {"populate_by_name": True}

    token: str


class ChangeUsernameRequestDTO(BaseModel):
    """DTO for changing username."""

    model_config = {"populate_by_name": True}

    new_username: str = Field(..., alias="newUsername", min_length=3, max_length=50)


class AssignRoleRequestDTO(BaseModel):
    """DTO for assigning role to user."""

    model_config = {"populate_by_name": True}

    user_id: str = Field(..., alias="userId")
    role: str


class RemoveRoleRequestDTO(BaseModel):
    """DTO for removing role from user."""

    model_config = {"populate_by_name": True}

    user_id: str = Field(..., alias="userId")
    role: str


# =============================================================================
# Response Models
# =============================================================================


class AuthUserDTO(BaseModel):
    """DTO for auth user."""

    model_config = {"populate_by_name": True}

    id: Optional[str] = None
    username: Optional[str] = None
    email: Optional[EmailStr] = None
    roles: Optional[List[str]] = None
    created_at: Optional[str] = Field(None, alias="createdAt")
    updated_at: Optional[str] = Field(None, alias="updatedAt")
    email_verified: Optional[bool] = Field(None, alias="emailVerified")


class AuthResponseDTO(BaseModel):
    """DTO for auth response with tokens."""

    model_config = {"populate_by_name": True}

    user: Optional[AuthUserDTO] = None
    access_token: Optional[str] = Field(None, alias="accessToken")
    refresh_token: Optional[str] = Field(None, alias="refreshToken")
    expires_in: Optional[int] = Field(None, alias="expiresIn")
    token_type: Optional[str] = Field(None, alias="tokenType")


class ApiResponseWrapper(BaseModel):
    """Wrapper for API responses from backend."""

    model_config = {"populate_by_name": True}

    status: int
    message: str
    data: Optional[dict] = None
    timestamp: Optional[str] = None


# =============================================================================
# Test Data Constants
# =============================================================================

TEST_USER = {"username": "testuser", "email": "test@example.com", "password": "Test123456"}

TEST_USER_CREDENTIALS = {"email": "test@example.com", "password": "Test123456"}


# =============================================================================
# Validation Patterns
# =============================================================================

VALIDATION_PATTERNS = {
    "uuid": r"^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
    "iso_timestamp": r"^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?(Z|[+-]\d{2}:\d{2})?$",
    "jwt_token": r"^[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+\.[A-Za-z0-9-_]*$",
}

# Default values
DEFAULTS = {
    "timeout": 30000,
    "max_retries": 3,
}
