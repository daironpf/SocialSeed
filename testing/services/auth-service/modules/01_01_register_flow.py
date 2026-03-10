"""Test module for user registration flow.

This module implements the test flow for user registration.
"""

import uuid
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from services.auth_service.auth_service_page import AuthServicePage


def run(auth_service: "AuthServicePage") -> APIResponse:
    """Execute user registration test flow.

    This test validates the user registration functionality.

    Args:
        auth_service: Instance of AuthServicePage

    Returns:
        APIResponse: HTTP response from the API

    Raises:
        AssertionError: If test assertions fail
    """
    print(f"Running user registration test...")

    # Generate unique user data
    unique_id = str(uuid.uuid4())[:8]
    test_email = f"testuser_{unique_id}@example.com"
    test_username = f"testuser_{unique_id}"
    test_password = "Test123456"

    # Register new user
    response = auth_service.register(
        username=test_username, email=test_email, password=test_password
    )

    # Assert: Verify registration was successful
    assert response.ok, f"Registration failed with status {response.status}: {response.text()}"

    body = response.json()
    assert body.get("status") == 200 or body.get("status") == 201, (
        f"Registration status error: {body}"
    )
    assert body.get("data") is not None, "No data in response"
    assert body["data"].get("accessToken") is not None, "No access token in response"

    # Store tokens for subsequent tests
    auth_service.access_token = body["data"].get("accessToken")
    auth_service.refresh_token = body["data"].get("refreshToken")
    if body["data"].get("user"):
        auth_service.user_id = body["data"]["user"].get("id")
        auth_service.current_user = body["data"]["user"]

    print(f"✓ User registered: {test_email}")
    print(f"✓ Access token received")
    return response
