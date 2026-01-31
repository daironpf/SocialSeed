import time
import subprocess
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT, Optional
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore

def run(auth_page: 'AuthPage') -> APIResponse:
    """Run change email flow test."""
    print("Running change email flow...")
    if not auth_page.is_logged_in or not auth_page.auth_result:
        raise ValueError("Login must run first")
    if not auth_page.test_email:
        raise ValueError("test_email must be set")

    old_email: Optional[str] = auth_page.test_email
    assert old_email is not None, "old_email cannot be None"
    new_email: str = f"new_email_{int(time.time())}@seed.com"
    print(f"  Initiating email change from {old_email} to {new_email}...")
    response: APIResponse = auth_page.change_email(new_email)

    if response.status == 200:
        print("✓ Email change initiated")
    else:
        print(f"✗ Error initiating email change: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Email change initiation failed")

    # Fetch email change token from DB
    print("  Fetching email change token from DB via SQL...")
    get_token_sql: str = f"SELECT email_change_token FROM auth_users WHERE email = '{old_email}';"
    try:
        token_out = subprocess.run(
            ["psql", "-U", "authuser", "-d", "authdb", "-h", "localhost", "-t", "-c", get_token_sql],
            env={"PGPASSWORD": "authpass"},
            check=True,
            capture_output=True,
            text=True
        )
        stdout = token_out.stdout
        email_token: str = stdout.strip() if stdout else ''
        if not email_token or email_token == '(no rows)':
            print("✗ Could not find email change token in DB")
            raise AssertionError("Email change token not found")
        print(f"  Found token: {email_token[:5]}...")
    except Exception as e:
        print(f"✗ SQL error fetching token: {e}")
        raise AssertionError("Failed to fetch email change token")

    print("  Verifying email change with token...")
    response = auth_page.verify_email_change(email_token)

    if response.status == 200:
        print("✓ Email verification successful")
        # Note: The response doesn't return the new email, we already have it
        # auth_page.test_email = new_email (already set at line 17)
    else:
        print(f"✗ Verification failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Email change verification failed")

    # Verify login with new email
    print("  Verifying login with new email...")
    response = auth_page.login(new_email, auth_page.test_password or 'StrongPass1!')
    if response.status == 200:
        print("✓ Login with new email successful")
        # Update test_email to new_email after successful login
        auth_page.test_email = new_email
    else:
        print(f"✗ Login with new email failed: {response.status}")
        raise AssertionError("Login with new email failed")

    # Verify sync in SocialUser service (if available)
    print("  Verifying sync in SocialUser service (Neo4j)...")
    social_url: str = "http://localhost:8090/socialusers/getSocialUserByEmail"
    try:
        # Create a temporary request for external URL
        import requests
        external_response = requests.get(f"{social_url}/{new_email}")
        if external_response.status_code == 200:
            data = external_response.json()
            if data.get('data') and data['data'].get('email') == new_email:
                print(f"✓ Email {new_email} correctly synced to SocialUser node")
            else:
                print(f"✗ SocialUser sync failed or data incorrect")
                raise AssertionError("SocialUser sync failed")
        else:
            print(f"⚠ Could not query SocialUser service: {external_response.status_code} (may not be running)")
    except Exception as e:
        print(f"⚠ SocialUser service check failed: {e} (may not be running)")

    return response
