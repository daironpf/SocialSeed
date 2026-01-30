from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore


if TC_IMPORT:
    pass


def run(auth_page: 'AuthPage') -> APIResponse:
    """Run resend verification flow test."""
    print("Running resend verification flow...")

    if not auth_page.test_email:
        raise ValueError("test_email must be set")

    print(f"  Resending verification email to {auth_page.test_email}...")
    response: APIResponse = auth_page.resend_verification(auth_page.test_email)

    if response.status == 200:
        print("✓ Resend verification request successful")
    elif response.status == 400:
        # Check if it's because email is already verified
        response_text = AuthPage.get_response_text(response)
        if "already verified" in response_text.lower() or "verificado" in response_text.lower():
            print("✓ Resend verification: Email already verified (expected)")
        else:
            print(f"✗ Resend verification failed: {response.status} - {response_text}")
            raise AssertionError("Resend verification failed")
    else:
        print(f"✗ Resend verification failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Resend verification failed")

    return response
