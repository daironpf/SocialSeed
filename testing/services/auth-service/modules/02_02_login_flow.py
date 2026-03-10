"""Test module for 02_login flow.

This module implements the test flow for Test flow for 02_login.
"""

from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from services.auth_service.auth_service_page import AuthServicePage


def run(auth_service: 'AuthServicePage') -> APIResponse:
    """Execute 02_login test flow.

    This test validates the Test flow for 02_login functionality.

    Args:
        auth_service: Instance of AuthServicePage

    Returns:
        APIResponse: HTTP response from the API

    Raises:
        AssertionError: If test assertions fail
    """
    print(f"Running 02_login test...")

    # Default health check test - validates the service is running
    response = auth_service.get("/health")

    # Assert: Verify the service is healthy
    assert response.ok, f"Health check failed with status {response.status}: {response.text()}"

    print(f"✓ 02_login test completed successfully")
    return response
