"""Test module for Auth Service Login."""

from services.auth_pages import AuthPage
import pytest
import time


class TestAuthLogin:
    """Login tests for Auth Service."""

    @pytest.fixture
    def registered_user(self, auth_page: AuthPage) -> dict:
        """Fixture providing a registered user."""
        ts = int(time.time()) % 100000
        suffix = "a"
        user = {
            "email": f"login_{ts}{suffix}@example.com",
            "username": f"login_{ts}{suffix}",
            "password": "Test1234!"
        }
        resp = auth_page.register(**user)
        return user

    def test_login_success(self, auth_page: AuthPage, registered_user: dict) -> None:
        """Test successful login returns tokens."""
        response = auth_page.login(
            email=registered_user["email"],
            password=registered_user["password"]
        )
        
        assert response.status == 200
        data = response.json()
        assert data.get("status") == 200
        assert "data" in data
        assert "token" in data["data"] or "accessToken" in data["data"]
        assert "refreshToken" in data["data"]

    def test_login_wrong_password(self, auth_page: AuthPage, registered_user: dict) -> None:
        """Test login with wrong password fails."""
        response = auth_page.login(
            email=registered_user["email"],
            password="WrongPassword123!"
        )
        assert response.status == 401

    def test_login_nonexistent_user(self, auth_page: AuthPage) -> None:
        """Test login with nonexistent email fails."""
        response = auth_page.login(
            email="nonexistent@example.com",
            password="Test1234!"
        )
        assert response.status == 401

    def test_login_invalid_email_format(self, auth_page: AuthPage) -> None:
        """Test login with invalid email format fails."""
        response = auth_page.login(
            email="invalid-email",
            password="Test1234!"
        )
        assert response.status == 400

    def test_login_missing_fields(self, auth_page: AuthPage) -> None:
        """Test login with missing fields fails."""
        response = auth_page.login(email="test@example.com", password="")
        assert response.status == 400
        
        response = auth_page.login(email="", password="Test1234!")
        assert response.status == 400
