"""Pytest configuration for Auth Service tests."""

import pytest

from services.auth_pages import AuthPage


@pytest.fixture(scope="session")
def auth_url() -> str:
    """Base URL for auth service."""
    return "http://localhost:8085"


@pytest.fixture(scope="function")
def auth_page(auth_url) -> AuthPage:
    """Fixture providing AuthPage instance with setup/teardown."""
    page = AuthPage(base_url=auth_url)
    page.setup()
    yield page
    page.teardown()


@pytest.fixture(scope="session")
def test_user() -> dict:
    """Fixture providing test user data."""
    import time
    ts = int(time.time()) % 100000
    suffix = "a"
    return {
        "email": f"test_{ts}{suffix}@example.com",
        "username": f"test_{ts}{suffix}",
        "password": "Test1234!"
    }
