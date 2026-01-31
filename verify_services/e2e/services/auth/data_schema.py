from pydantic import BaseModel
from typing import Optional, List


class UserDTO(BaseModel):
    id: str
    username: str
    email: str
    roles: List[str] = []


class LoginRequest(BaseModel):
    email: str
    password: str
    ip: Optional[str] = None


class RegisterRequest(BaseModel):
    id: Optional[str] = None
    username: str
    email: str
    password: str


class AuthResult(BaseModel):
    token: str
    refreshToken: str
    roles: Optional[List[str]] = []


class ChangePasswordRequest(BaseModel):
    currentPassword: str
    newPassword: str


class ForgotPasswordRequest(BaseModel):
    email: str


class ResetPasswordRequest(BaseModel):
    token: str
    newPassword: str


class VerifyEmailRequest(BaseModel):
    token: str


class ChangeUsernameRequest(BaseModel):
    newUsername: str


class ChangeEmailRequest(BaseModel):
    newEmail: str


class VerifyEmailChangeRequest(BaseModel):
    token: str


class ResendVerificationRequest(BaseModel):
    email: str


# Endpoints (relative to base_url)
LOGIN_ENDPOINT = "/login"
REGISTER_ENDPOINT = "/register"
LOGOUT_ENDPOINT = "/logout"
REFRESH_ENDPOINT = "/token/refresh"
CHANGE_PASSWORD_ENDPOINT = "/{user_id}/change-password"
FORGOT_PASSWORD_ENDPOINT = "/forgot-password"
RESET_PASSWORD_ENDPOINT = "/reset-password"
VERIFY_EMAIL_ENDPOINT = "/verify-email"
VERIFY_EMAIL_GET_ENDPOINT = "/verify"
CHANGE_USERNAME_ENDPOINT = "/username"
CHANGE_EMAIL_ENDPOINT = "/change-email"
VERIFY_EMAIL_CHANGE_ENDPOINT = "/verify-email-change"
RESEND_VERIFICATION_ENDPOINT = "/resend-verification"
GET_USER_BY_EMAIL_ENDPOINT = "/getUserByEmail/{email}"
GET_USER_BY_USERNAME_ENDPOINT = "/getUserByUserName/{username}"