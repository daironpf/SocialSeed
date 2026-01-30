from verify_services.e2e.services.auth.data_schema import RegisterRequest
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore


if TC_IMPORT:
    pass


def run(auth_page: 'AuthPage') -> APIResponse:
    """Run registration flow test."""
    print("Running registration flow...")
    # Generate unique data
    import time
    unique_suffix: int = int(time.time()) % 10000
    email: str = f"user_{unique_suffix}@strong.com"
    username: str = f"stronguser_{unique_suffix}"
    password: str = "StrongPass1!"

    print(f"  Attempting to register user: {username}, email: {email}")
    print(f"  AuthPage base_url: {auth_page.base_url}")
    print(f"  AuthPage api_context: {auth_page.api_context}")

    request = RegisterRequest(id=None, username=username, email=email, password=password)
    response: APIResponse = auth_page.register(request.username, request.email, request.password, request.id)

    print(f"  Response status: {response.status}")
    print(f"  Response headers: {response.headers}")
    body = response.body()
    print(f"  Response body length: {len(body)}")
    response_text = AuthPage.get_response_text(response)
    print(f"  Response body: {response_text}")

    if response.status in [200, 201]:
        print("✓ Registration successful")
        # Store for later use
        auth_page.test_email = email
        auth_page.test_username = username
        auth_page.test_password = password
    else:
        print(f"✗ Registration failed: {response.status} - {response_text}")
        raise AssertionError("Registration failed")

    return response
