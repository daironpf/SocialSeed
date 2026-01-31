import time
import subprocess
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore

def run(auth_page: 'AuthPage') -> APIResponse:
    """Run verify email flow test."""
    print("Running verify email flow...")

    # Create a new user specifically for this test
    unique_suffix: int = int(time.time())
    email: str = f"verify_{unique_suffix}@test.com"
    username: str = f"verifyuser_{unique_suffix % 10000}"
    password: str = "StrongPass1!"

    print(f"  Registering user {username} ({email})...")
    response: APIResponse = auth_page.register(username, email, password)
    if response.status not in [200, 201]:
        print(f"✗ Registration failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Registration failed")
    print("  Registration successful")

    # Fetch verification token from DB
    print("  Fetching verification token from DB via SQL...")
    get_token_sql: str = f"SELECT verification_token FROM auth_users WHERE email = '{email}';"
    try:
        token_out = subprocess.run(
            ["psql", "-U", "authuser", "-d", "authdb", "-h", "localhost", "-t", "-c", get_token_sql],
            env={"PGPASSWORD": "authpass"},
            check=True,
            capture_output=True,
            text=True
        )
        verify_token: str = token_out.stdout.strip()
        if not verify_token or verify_token == "no":
            print("✗ Could not find verification token in DB")
            raise AssertionError("Verification token not found")
        print(f"  Found token: {verify_token[:5]}...")
    except Exception as e:
        print(f"✗ SQL error fetching token: {e}")
        raise AssertionError("Failed to fetch verification token")

    # Test GET /auth/verify?token=... endpoint
    print("  Testing GET /auth/verify?token=... endpoint...")
    response = auth_page.verify_email(verify_token, use_get=True)

    if response.status == 200:
        print("✓ Email verification via GET successful")
    else:
        print(f"✗ GET verify failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("GET verify failed")

    # Verify user is now marked as verified in DB
    print("  Verifying emailVerified flag in DB...")
    check_sql: str = f"SELECT email_verified FROM auth_users WHERE email = '{email}';"
    try:
        check_out = subprocess.run(
            ["psql", "-U", "authuser", "-d", "authdb", "-h", "localhost", "-t", "-c", check_sql],
            env={"PGPASSWORD": "authpass"},
            check=True,
            capture_output=True,
            text=True
        )
        is_verified: str = check_out.stdout.strip()
        if is_verified == "t":
            print("✓ Email verified = true in database")
        else:
            print(f"✗ Email verified is not true. Value: {is_verified}")
            raise AssertionError("Email not marked as verified")
    except Exception as e:
        print(f"✗ SQL error checking emailVerified: {e}")
        raise AssertionError("Failed to check emailVerified")

    # Test invalid token (should return 400)
    print("  Testing invalid token (should return 400)...")
    response = auth_page.verify_email("invalid-fake-token", use_get=True)
    if response.status == 400:
        print("✓ Invalid token correctly rejected")
    else:
        print(f"✗ Expected 400 for invalid token, got {response.status}")
        raise AssertionError("Invalid token should return 400")

    # Test already verified (should return 400)
    print("  Testing already verified user (should return 400)...")
    response = auth_page.verify_email(verify_token, use_get=True)
    if response.status == 400:
        print("✓ Already verified user correctly rejected")
    else:
        print(f"✗ Expected 400 for already verified, got {response.status}")
        raise AssertionError("Already verified should return 400")

    return response
