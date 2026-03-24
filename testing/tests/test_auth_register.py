"""Test module for Auth Service Registration."""

from services.auth_pages import AuthPage
import pytest
import time


class TestAuthRegister:
    """Registration tests for Auth Service."""

    def test_register_success(self, auth_page: AuthPage) -> None:
        """Test successful user registration."""
        ts = int(time.time()) % 100000
        suffix = "a"
        payload = {
            "email": f"newu_{ts}{suffix}@example.com",
            "username": f"newu_{ts}{suffix}",
            "password": "Test1234!"
        }
        response = auth_page.register(**payload)
        
        assert response.status in [200, 201]
        data = response.json()
        assert data.get("status") in [200, 201]
        assert "data" in data
        assert "token" in data["data"] or "accessToken" in data["data"]

    def test_register_duplicate_email(self, auth_page: AuthPage) -> None:
        """Test registration with duplicate email fails."""
        ts = int(time.time()) % 100000
        suffix = "b"
        email = f"dup_{ts}{suffix}@example.com"
        
        payload = {
            "email": email,
            "username": f"u1_{ts}{suffix}",
            "password": "Test1234!"
        }
        response1 = auth_page.register(**payload)
        assert response1.status == 201
        
        payload["username"] = f"u2_{ts}{suffix}"
        response2 = auth_page.register(**payload)
        assert response2.status in [400, 409]

    def test_register_duplicate_username(self, auth_page: AuthPage) -> None:
        """Test registration with duplicate username fails."""
        ts = int(time.time()) % 100000
        suffix = "c"
        username = f"dupu_{ts}{suffix}"
        
        payload = {
            "email": f"e1_{ts}{suffix}@example.com",
            "username": username,
            "password": "Test1234!"
        }
        response1 = auth_page.register(**payload)
        assert response1.status == 201
        
        payload["email"] = f"e2_{ts}{suffix}@example.com"
        response2 = auth_page.register(**payload)
        assert response2.status in [400, 409]

    def test_register_invalid_email(self, auth_page: AuthPage) -> None:
        """Test registration with invalid email fails."""
        ts = int(time.time()) % 100000
        payload = {
            "email": "invalid-email",
            "username": f"u_{ts}d",
            "password": "Test1234!"
        }
        response = auth_page.register(**payload)
        assert response.status == 400

    def test_register_weak_password(self, auth_page: AuthPage) -> None:
        """Test registration with weak password fails."""
        ts = int(time.time()) % 100000
        payload = {
            "email": f"u_{ts}e@example.com",
            "username": f"wk_{ts}e",
            "password": "123"
        }
        response = auth_page.register(**payload)
        assert response.status == 400

    def test_register_missing_fields(self, auth_page: AuthPage) -> None:
        """Test registration with missing fields fails."""
        ts = int(time.time()) % 100000
        
        payload = {"email": f"u_{ts}f@example.com"}
        response = auth_page._post("/auth/register", json=payload)
        assert response.status == 400
        
        payload = {"username": f"u_{ts}f"}
        response = auth_page._post("/auth/register", json=payload)
        assert response.status == 400
        
        payload = {"password": "Test1234!"}
        response = auth_page._post("/auth/register", json=payload)
        assert response.status == 400
