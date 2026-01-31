import time
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore

def run(auth_page: 'AuthPage') -> APIResponse:
    """Run change username flow test."""
    print("Running change username flow...")
    if not auth_page.is_logged_in or not auth_page.auth_result:
        raise ValueError("Login must run first")

    new_username: str = f"cool_user_{int(time.time())}"
    print(f"  Changing username to {new_username}...")
    response: APIResponse = auth_page.change_username(new_username)

    if response.status == 200:
        print("✓ Username change successful")
        auth_page.test_username = new_username
    else:
        print(f"✗ Change username failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Change username failed")

    # Verify lookup by new username
    print(f"  Verifying lookup by new username {new_username}...")
    response = auth_page.get_user_by_username(new_username)
    if response.status == 200:
        data = response.json()
        user_data = data.get('data', data)
        if user_data.get('username') == new_username:
            print("✓ Lookup by new username successful")
        else:
            print(f"✗ Username in response doesn't match: {user_data.get('username')}")
            raise AssertionError("Username mismatch")
    else:
        print(f"✗ Lookup by new username failed: {response.status}")
        raise AssertionError("Lookup by new username failed")

    return response
