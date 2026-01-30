import time
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore

def run(auth_page: 'AuthPage') -> APIResponse:
    """Run forgot password flow test."""
    print("Running forgot password flow...")

    # Create a new user specifically for this test
    unique_suffix: int = int(time.time()) % 10000
    email: str = f"forgot_{unique_suffix}@test.com"
    username: str = f"forgotuser_{unique_suffix}"
    password: str = "StrongPass1!"

    print(f"  Registering user {username} ({email})...")
    response: APIResponse = auth_page.register(username, email, password)
    if response.status not in [200, 201]:
        print(f"✗ Registration failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Registration failed")
    print("  Registration successful")

    print(f"  Initiating forgot password for {email}...")
    response = auth_page.forgot_password(email)

    if response.status == 200:
        print("✓ Forgot password request successful")
        # Store email for later reference
        auth_page.test_forgot_email = email
        auth_page.test_forgot_password = password
    else:
        print(f"✗ Forgot password failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Forgot password failed")

    return response
