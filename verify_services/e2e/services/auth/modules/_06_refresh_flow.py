from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore


if TC_IMPORT:
    pass


def run(auth_page: 'AuthPage') -> APIResponse:
    """Run token refresh flow test."""
    print("Running token refresh flow...")
    if not auth_page.is_logged_in or not auth_page.auth_result:
        raise ValueError("Login must run first")

    # Access refresh_token safely
    refresh_token = auth_page.auth_result.refreshToken if hasattr(auth_page.auth_result, 'refreshToken') else None
    if not refresh_token:
        raise ValueError("No refresh token available")

    response: APIResponse = auth_page.refresh_token()

    if response.status == 200:
        print("✓ Token refresh successful")
        # auth_page.auth_result is already updated in refresh_token
    else:
        print(f"✗ Token refresh failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Token refresh failed")

    return response
