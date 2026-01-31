from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore

def run(auth_page: 'AuthPage') -> APIResponse:
    """Run reset password flow test (negative test with invalid token)."""
    print("Running reset password flow (negative test)...")

    print("  Testing reset password with invalid token...")
    response: APIResponse = auth_page.reset_password("invalid_token_simulation", "AnotherPassword123!")

    if response.status == 400:
        print("✓ Reset password with invalid token rejected as expected")
    else:
        print(f"✗ Unexpected status for invalid token: {response.status}")
        raise AssertionError("Invalid token should be rejected")

    return response
