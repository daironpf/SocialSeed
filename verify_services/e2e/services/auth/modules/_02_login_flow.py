from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore


if TC_IMPORT:
    pass


def run(auth_page: 'AuthPage') -> APIResponse:
    """Run login flow test."""
    print("Running login flow...")
    if not auth_page.test_email:
        raise ValueError("Registration must run first to set test_email")

    email: str = auth_page.test_email
    password: str = auth_page.test_password or 'StrongPass1!'

    response: APIResponse = auth_page.login(email, password)

    if response.status == 200:
        print("✓ Login successful")
        # auth_page.auth_result is already set by auth_page.login()
        # auth_page.is_logged_in is already set by auth_page.login()
    else:
        print(f"✗ Login failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Login failed")

    return response
