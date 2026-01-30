from verify_services.e2e.core.base_page import BasePage
from verify_services.e2e.services.auth.data_schema import UserDTO, AuthResult, AUTH_BASE_URL
from playwright.sync_api import APIResponse
from typing import Optional, TYPE_CHECKING

if TYPE_CHECKING:
    from playwright.sync_api import Playwright


class AuthPage(BasePage):
    """
    Hub for auth service: manages state, shared data, and orchestrates modules.
    """

    @staticmethod
    def get_response_text(response: APIResponse) -> str:
        """Get response text from Playwright APIResponse."""
        return BasePage.get_response_text(response)
    def __init__(self, playwright: Optional['Playwright'] = None) -> None:
        super().__init__(AUTH_BASE_URL, playwright)
        self.current_user: Optional[UserDTO] = None
        self.auth_result: Optional[AuthResult] = None
        self.is_logged_in: bool = False
        # Test data
        self.test_email: Optional[str] = None
        self.test_username: Optional[str] = None
        self.test_password: Optional[str] = None
        self.user_id: Optional[str] = None
        self.unique_suffix: Optional[int] = None
        self.test_forgot_email: Optional[str] = None
        self.test_forgot_password: Optional[str] = None

    def login(self, email: str, password: str, ip: Optional[str] = None) -> APIResponse:
        """Login and update state."""
        response = self.post("/login", {"email": email, "password": password, "ip": ip})
        if response.ok:
            full_data = response.json()
            data = full_data.get('data', full_data)
            # Convert dict to AuthResult object
            self.auth_result = AuthResult(**data) if isinstance(data, dict) else data
            self.is_logged_in = True
            # Store email for reference
            if not self.test_email:
                self.test_email = email
            # Assume user data from token or separate call
            self.current_user = UserDTO(id="temp", username="temp", email=email, roles=self.auth_result.roles or [])
        return response

    def register(self, username: str, email: str, password: str, user_id: Optional[str] = None) -> APIResponse:
        """Register and update state."""
        data = {"username": username, "email": email, "password": password}
        if user_id:
            data["id"] = user_id
        response = self.post("/register", data)
        if response.ok:
            # Registration might not log in immediately
            self.test_email = email
            self.test_username = username
            self.test_password = password
            self.user_id = user_id
        return response

    def logout(self) -> APIResponse:
        """Logout and clear state."""
        headers = {"Authorization": f"Bearer {self.auth_result.token}"} if self.auth_result else {}
        data = {"refreshToken": self.auth_result.refreshToken} if self.auth_result else {}
        response = self.post("/logout", data=data, headers=headers)
        if response.ok:
            self.is_logged_in = False
            self.current_user = None
            self.auth_result = None
        return response

    def refresh_token(self) -> APIResponse:
        """Refresh token."""
        if not self.auth_result:
            raise ValueError("No auth result available")
        response = self.post("/token/refresh", {"refreshToken": self.auth_result.refreshToken})
        if response.ok:
            full_data = response.json()
            data = full_data.get('data', full_data)
            self.auth_result = AuthResult(**data)
        return response

    def change_password(self, user_id: str, current_password: str, new_password: str) -> APIResponse:
        """Change user password."""
        headers = {"Authorization": f"Bearer {self.auth_result.token}"} if self.auth_result else {}
        response = self.post(f"/{user_id}/change-password", {
            "currentPassword": current_password,
            "newPassword": new_password
        }, headers=headers)
        if response.ok:
            self.test_password = new_password
        return response

    def forgot_password(self, email: str) -> APIResponse:
        """Initiate forgot password flow."""
        response = self.post("/forgot-password", {"email": email})
        return response

    def reset_password(self, token: str, new_password: str) -> APIResponse:
        """Reset password with token."""
        response = self.post("/reset-password", {"token": token, "newPassword": new_password})
        return response

    def verify_email(self, token: str, use_get: bool = False) -> APIResponse:
        """Verify email with token."""
        if use_get:
            response = self.get("/verify", params={"token": token})
        else:
            response = self.post("/verify-email", {"token": token})
        return response

    def change_username(self, new_username: str) -> APIResponse:
        """Change username."""
        headers = {"Authorization": f"Bearer {self.auth_result.token}"} if self.auth_result else {}
        response = self.patch("/username", {"newUsername": new_username}, headers=headers)
        if response.ok:
            self.test_username = new_username
        return response

    def change_email(self, new_email: str) -> APIResponse:
        """Initiate email change."""
        headers = {"Authorization": f"Bearer {self.auth_result.token}"} if self.auth_result else {}
        response = self.post("/change-email", {"newEmail": new_email}, headers=headers)
        return response

    def verify_email_change(self, token: str) -> APIResponse:
        """Verify email change with token."""
        response = self.post("/verify-email-change", {"token": token})
        # Note: The verify endpoint returns null in data, so we don't update test_email from response
        return response

    def resend_verification(self, email: str) -> APIResponse:
        """Resend verification email."""
        response = self.post("/resend-verification", {"email": email})
        return response

    def get_user_by_email(self, email: str) -> APIResponse:
        """Get user by email."""
        response = self.get(f"/getUserByEmail/{email}")
        if response.ok:
            full_data = response.json()
            data = full_data.get('data', full_data)
            if 'id' in data:
                self.user_id = data['id']
        return response

    def get_user_by_username(self, username: str) -> APIResponse:
        """Get user by username."""
        response = self.get(f"/getUserByUserName/{username}")
        return response