from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore


def run(auth_page: 'AuthPage') -> APIResponse:
    """Run change password flow test."""
    print("Running change password flow...")
    if not auth_page.is_logged_in:
        raise ValueError("Login must run first to set is_logged_in")

    # Get user_id if not available (after logout, re-login should have set it)
    if not auth_page.user_id:
        # Try to get user by email to get the user_id
        response = auth_page.get_user_by_email(auth_page.test_email or '')
        if response.status != 200 or not auth_page.user_id:
            raise ValueError("Could not get user_id. Ensure login has been performed.")
        print(f"  Retrieved user_id: {auth_page.user_id}")

    current_password: str = auth_page.test_password or 'StrongPass1!'
    new_password: str = 'NewSecretPassword123!' if current_password == 'StrongPass1!' else 'StrongPass1!'

    print(f"  Changing password from {current_password} to {new_password}...")
    response: APIResponse = auth_page.change_password(auth_page.user_id, current_password, new_password)

    if response.status == 200:
        print("✓ Password change successful")
        auth_page.test_password = new_password
    else:
        print(f"✗ Password change failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Password change failed")

    return response
