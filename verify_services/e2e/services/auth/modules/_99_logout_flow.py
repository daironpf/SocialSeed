from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore

def run(auth_page: 'AuthPage') -> APIResponse:
    """Run logout flow test."""
    print("Running logout flow...")
    if not auth_page.is_logged_in or not auth_page.auth_result:
        raise ValueError("Login must run first")

    print("  Logging out...")
    response: APIResponse = auth_page.logout()

    if response.status in [200, 204]:
        print("✓ Logout successful")
        # State is already cleared in logout method
    else:
        print(f"✗ Logout failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Logout failed")

    return response
