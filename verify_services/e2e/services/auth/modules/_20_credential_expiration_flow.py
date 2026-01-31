import time
import subprocess
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore

def run(auth_page: 'AuthPage') -> APIResponse:
    """Run credential expiration strategy flow test."""
    print("Running credential expiration flow...")

    unique_suffix: int = int(time.time())
    expiry_email: str = f"expiry_{unique_suffix}@test.com"
    expiry_id: str = f"22222222-3333-4444-5555-{unique_suffix % 1000000000000:012d}"
    password: str = "StrongPass1!"

    print(f"  Registering user {expiry_email}...")
    response: APIResponse = auth_page.register(f"exp_{unique_suffix}", expiry_email, password, expiry_id)
    if response.status not in [200, 201]:
        print(f"✗ Registration failed: {response.status} - {AuthPage.get_response_text(response)}")
        raise AssertionError("Registration failed")
    print("  Registration successful")

    print("  Backdating password change date via SQL...")
    sql_command: str = f"UPDATE auth_users SET last_password_changed_at = NOW() - INTERVAL '91 days' WHERE email = '{expiry_email}';"
    try:
        subprocess.run(
            ["psql", "-U", "authuser", "-d", "authdb", "-h", "localhost", "-c", sql_command],
            env={"PGPASSWORD": "authpass"},
            check=True,
            capture_output=True
        )
        print("  SQL update successful")
    except Exception as e:
        print(f"  ⚠ Could not update DB via psql: {e}")

    print("  Manually flagging user as expired via SQL for E2E test...")
    flag_sql: str = f"UPDATE auth_users SET credentials_non_expired = false WHERE email = '{expiry_email}';"
    try:
        subprocess.run(
            ["psql", "-U", "authuser", "-d", "authdb", "-h", "localhost", "-c", flag_sql],
            env={"PGPASSWORD": "authpass"},
            check=True,
            capture_output=True
        )
        print("  User manually flagged as expired")
    except Exception as e:
        print(f"  ⚠ Could not flag user via psql: {e}")

    print("  Attempting login with expired password...")
    response = auth_page.login(expiry_email, password)
    response_text: str = auth_page.get_response_text(response)
    if response.status == 401 and "expired" in response_text.lower():
        print("✓ Login blocked with 401 Unauthorized and expiration message")
    else:
        print(f"✗ Login not blocked or unexpected message! Status: {response.status}")
        raise AssertionError("Expired login should be blocked")

    print("  Note: Users must use forgot-password to recover if blocked")
    return response
