"""Service page for auth-service.

This module defines the page class to interact with the auth-service service.

⚠️  IMPORTANT:
- Use ABSOLUTE imports, never relative
- To serialize Pydantic models, ALWAYS use: request.model_dump(by_alias=True)
- Handle headers manually (update_headers does not exist in BasePage)
"""

from typing import Optional, Dict, Any
from playwright.sync_api import APIResponse

from socialseed_e2e.core.base_page import BasePage

from .data_schema import (
    ENDPOINTS,
    RegisterRequestDTO,
    LoginRequestDTO,
    LogoutRequestDTO,
    RefreshTokenRequestDTO,
    ChangePasswordRequestDTO,
    ForgotPasswordRequestDTO,
    ResetPasswordRequestDTO,
    VerifyEmailRequestDTO,
    ResendVerificationRequestDTO,
    ChangeEmailRequestDTO,
    ChangeUsernameRequestDTO,
)


class AuthServicePage(BasePage):
    """Service page for auth-service.

    Manages shared state between test modules.

    Attributes:
        current_user: Currently authenticated user
        access_token: Access token for authentication
        refresh_token: Refresh token for getting new access tokens
    """

    def __init__(self, base_url: str, **kwargs):
        """Initializes the service page.

        Args:
            base_url: Service base URL (e.g., http://localhost:8085)
            **kwargs: Additional arguments for BasePage
        """
        super().__init__(base_url=base_url, **kwargs)

        # Shared state between modules
        self.current_user: Optional[dict] = None
        self.access_token: Optional[str] = None
        self.refresh_token: Optional[str] = None
        self.user_id: Optional[str] = None

    def _get_headers(self, extra_headers: Optional[Dict[str, str]] = None) -> Dict[str, str]:
        """Gets headers including authentication token if available.

        Returns:
            Dict with HTTP headers
        """
        headers = {"Content-Type": "application/json", "Accept": "application/json"}
        if self.access_token:
            headers["Authorization"] = f"Bearer {self.access_token}"
        if extra_headers:
            headers.update(extra_headers)
        return headers

    # =========================================================================
    # User Operations
    # =========================================================================

    def get_user_by_id(self, user_id: str) -> APIResponse:
        """Get user by ID.

        Args:
            user_id: User UUID

        Returns:
            APIResponse: HTTP response
        """
        path = ENDPOINTS["get_user_by_id"].format(id=user_id)
        return self.get(path, headers=self._get_headers())

    def get_user_by_email(self, email: str) -> APIResponse:
        """Get user by email.

        Args:
            email: User email

        Returns:
            APIResponse: HTTP response
        """
        path = ENDPOINTS["get_user_by_email"].format(email=email)
        return self.get(path, headers=self._get_headers())

    def get_user_by_username(self, username: str) -> APIResponse:
        """Get user by username.

        Args:
            username: Username

        Returns:
            APIResponse: HTTP response
        """
        path = ENDPOINTS["get_user_by_username"].format(username=username)
        return self.get(path, headers=self._get_headers())

    # =========================================================================
    # Authentication Operations
    # =========================================================================

    def register(self, username: str, email: str, password: str) -> APIResponse:
        """Register a new user.

        Args:
            username: Username
            email: Email address
            password: Password

        Returns:
            APIResponse: HTTP response
        """
        data = RegisterRequestDTO(username=username, email=email, password=password)
        return self.post(ENDPOINTS["register"], data=data.model_dump(by_alias=True))

    def login(self, email: str, password: str) -> APIResponse:
        """Login user.

        Args:
            email: Email address
            password: Password

        Returns:
            APIResponse: HTTP response with tokens
        """
        data = LoginRequestDTO(email=email, password=password)
        response = self.post(ENDPOINTS["login"], data=data.model_dump(by_alias=True))

        # Store tokens if login successful
        if response.ok:
            try:
                body = response.json()
                if body.get("data"):
                    self.access_token = body["data"].get("accessToken")
                    self.refresh_token = body["data"].get("refreshToken")
                    if body["data"].get("user"):
                        self.user_id = body["data"]["user"].get("id")
                        self.current_user = body["data"]["user"]
            except Exception:
                pass

        return response

    def logout(self) -> APIResponse:
        """Logout user.

        Returns:
            APIResponse: HTTP response
        """
        data = LogoutRequestDTO(refresh_token=self.refresh_token)
        return self.post(
            ENDPOINTS["logout"], data=data.model_dump(by_alias=True), headers=self._get_headers()
        )

    def refresh_token_method(self, refresh_token: str) -> APIResponse:
        """Refresh access token.

        Args:
            refresh_token: Refresh token

        Returns:
            APIResponse: HTTP response with new tokens
        """
        data = RefreshTokenRequestDTO(refresh_token=refresh_token)
        return self.post(ENDPOINTS["refresh_token"], data=data.model_dump(by_alias=True))

    def change_password(
        self, user_id: str, current_password: str, new_password: str
    ) -> APIResponse:
        """Change user password.

        Args:
            user_id: User UUID
            current_password: Current password
            new_password: New password

        Returns:
            APIResponse: HTTP response
        """
        data = ChangePasswordRequestDTO(
            current_password=current_password, new_password=new_password
        )
        path = ENDPOINTS["change_password"].format(id=user_id)
        return self.post(path, data=data.model_dump(by_alias=True), headers=self._get_headers())

    # =========================================================================
    # Password Recovery
    # =========================================================================

    def forgot_password(self, email: str) -> APIResponse:
        """Request password reset.

        Args:
            email: Email address

        Returns:
            APIResponse: HTTP response
        """
        data = ForgotPasswordRequestDTO(email=email)
        return self.post(ENDPOINTS["forgot_password"], data=data.model_dump(by_alias=True))

    def reset_password(self, token: str, new_password: str) -> APIResponse:
        """Reset password with token.

        Args:
            token: Reset token
            new_password: New password

        Returns:
            APIResponse: HTTP response
        """
        data = ResetPasswordRequestDTO(token=token, new_password=new_password)
        return self.post(ENDPOINTS["reset_password"], data=data.model_dump(by_alias=True))

    # =========================================================================
    # Email Verification
    # =========================================================================

    def verify_email(self, token: str) -> APIResponse:
        """Verify email with token.

        Args:
            token: Verification token

        Returns:
            APIResponse: HTTP response
        """
        data = VerifyEmailRequestDTO(token=token)
        return self.post(ENDPOINTS["verify_email"], data=data.model_dump(by_alias=True))

    def resend_verification(self, email: str) -> APIResponse:
        """Resend verification email.

        Args:
            email: Email address

        Returns:
            APIResponse: HTTP response
        """
        data = ResendVerificationRequestDTO(email=email)
        return self.post(ENDPOINTS["resend_verification"], data=data.model_dump(by_alias=True))

    # =========================================================================
    # Utility Methods
    # =========================================================================

    def is_authenticated(self) -> bool:
        """Check if there's an access token available.

        Returns:
            bool: True if token exists, False otherwise
        """
        return self.access_token is not None

    def clear_auth(self) -> None:
        """Clear authentication state."""
        self.access_token = None
        self.refresh_token = None
        self.current_user = None
        self.user_id = None
