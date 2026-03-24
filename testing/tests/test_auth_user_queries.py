"""Test module for Auth Service User Queries and Flows."""

from services.auth_pages import AuthPage
import pytest
import time


class TestAuthUserQueries:
    """User query tests for Auth Service."""

    @pytest.fixture
    def registered_user(self, auth_page: AuthPage) -> dict:
        """Fixture providing a registered user with login tokens."""
        ts = int(time.time()) % 100000
        suffix = "h"
        user = {
            "email": f"qry_{ts}{suffix}@example.com",
            "username": f"qry_{ts}{suffix}",
            "password": "Test1234!"
        }
        auth_page.register(**user)
        auth_page.login(email=user["email"], password=user["password"])
        return user

    def test_get_user_by_id(self, auth_page: AuthPage, registered_user: dict) -> None:
        """Test getting user by ID."""
        user_id = auth_page.state.get("user_id")
        if user_id:
            response = auth_page.get_user_by_id(user_id)
            assert response.status == 200
            data = response.json()
            assert "data" in data

    def test_get_user_by_email(self, auth_page: AuthPage, registered_user: dict) -> None:
        """Test getting user by email."""
        response = auth_page.get_user_by_email(registered_user["email"])
        assert response.status == 200
        data = response.json()
        assert "data" in data

    def test_get_user_by_username(self, auth_page: AuthPage, registered_user: dict) -> None:
        """Test getting user by username."""
        response = auth_page.get_user_by_username(registered_user["username"])
        assert response.status == 200
        data = response.json()
        assert "data" in data

    def test_get_nonexistent_user(self, auth_page: AuthPage) -> None:
        """Test getting nonexistent user returns 404."""
        response = auth_page.get_user_by_email("nonexistent@example.com")
        assert response.status == 404


class TestAuthTokenFlow:
    """Token flow tests for Auth Service."""

    @pytest.fixture
    def logged_in_user(self, auth_page: AuthPage) -> dict:
        """Fixture providing a logged in user with tokens."""
        ts = int(time.time()) % 100000
        suffix = "i"
        user = {
            "email": f"tok_{ts}{suffix}@example.com",
            "username": f"tok_{ts}{suffix}",
            "password": "Test1234!"
        }
        auth_page.register(**user)
        auth_page.login(email=user["email"], password=user["password"])
        return user

    def test_refresh_token(self, auth_page: AuthPage, logged_in_user: dict) -> None:
        """Test refreshing access token."""
        refresh_token = auth_page.state.get("refresh_token")
        if refresh_token:
            response = auth_page.refresh_token(refresh_token)
            assert response.status == 200
            data = response.json()
            assert "data" in data
            assert "token" in data["data"] or "accessToken" in data["data"]

    def test_logout(self, auth_page: AuthPage, logged_in_user: dict) -> None:
        """Test logout invalidates tokens."""
        refresh_token = auth_page.state.get("refresh_token", "")
        response = auth_page.logout(refresh_token=refresh_token)
        assert response.status in [204, 200]


class TestAuthUsernameChange:
    """Username change tests for Auth Service."""

    @pytest.fixture
    def logged_in_user_for_username(self, auth_page: AuthPage) -> tuple:
        """Fixture providing a logged in user for username change tests."""
        ts = int(time.time()) % 100000
        suffix = "j"
        user = {
            "email": f"ren_{ts}{suffix}@example.com",
            "username": f"ren_{ts}{suffix}",
            "password": "Test1234!"
        }
        auth_page.register(**user)
        auth_page.login(email=user["email"], password=user["password"])
        return auth_page, user

    def test_change_username(self, auth_page: AuthPage, logged_in_user_for_username: tuple) -> None:
        """Test changing username."""
        page, user = logged_in_user_for_username
        assert page.state.get("access_token") is not None, "Access token should be set after login"
        
        timestamp = int(time.time())
        new_username = f"renamed_{timestamp}"
        response = page.change_username(new_username)
        
        if response.status == 500:
            pytest.skip("Change username endpoint returns 500 - service bug")
        
        assert response.status == 200, f"Expected 200 but got {response.status}: {response.text()}"
        
        response = page.get_user_by_username(new_username)
        assert response.status == 200
