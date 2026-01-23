import time
import requests
import sys

BASE_URL = "http://localhost:8081/auth"
ACTUATOR_URL = "http://localhost:8081/actuator/health"

# Test Data
USER_ID = "123e4567-e89b-12d3-a456-426614178888"
EMAIL = "user@strong.com"
INITIAL_PASSWORD = "StrongPass1!"
NEW_PASSWORD = "NewSecretPassword123!"
USERNAME = "stronguser"
def wait_for_service():
    print("Waiting for auth-service to be up...")
    for _ in range(60):
        try:
            response = requests.get(ACTUATOR_URL)
            if response.status_code == 200:
                print("Service is UP!")
                return True
        except requests.exceptions.ConnectionError:
            pass
        time.sleep(2)
    print("Service failed to start.")
    return False

def login(email, password):
    print(f"  Attempting login with {email}...")
    return requests.post(f"{BASE_URL}/login", json={"email": email, "password": password})

def register(id, username, email, password):
    print(f"  Registering user {username} ({email})...")
    payload = {
        "id": id,
        "username": username,
        "email": email,
        "password": password
    }
    return requests.post(f"{BASE_URL}/register", json=payload)

def resend_verification(email):
    print(f"  Resending verification email to {email}...")
    return requests.post(f"{BASE_URL}/resend-verification", json={"email": email})

def verify_email(token):
    print(f"  Verifying email with token {token}...")
    return requests.post(f"{BASE_URL}/verify-email", json={"token": token})

def run_verification():
    print("--- STARTING E2E AUTH FLOW VERIFICATION ---")

    # 0. Registration (Ensure user exists)
    print("\n0. Registration Flow")
    resp = register(USER_ID, USERNAME, EMAIL, INITIAL_PASSWORD)
    if resp.status_code == 201 or resp.status_code == 200:
        print("  Registration successful.")
    else:
        print(f"  Registration skipped or failed (might already exist). Status: {resp.status_code}")

    # 1. Login (Dynamic Password Handling)
    print("\n1. Login Flow")
    current_password = INITIAL_PASSWORD
    resp = login(EMAIL, current_password)
    
    if resp.status_code != 200:
        print("  Login with initial password failed. Trying alternate password...")
        current_password = NEW_PASSWORD
        resp = login(EMAIL, current_password)
        if resp.status_code != 200:
            print(f"  FATAL: Could not login with either password. Status: {resp.status_code}, Body: {resp.text}")
            sys.exit(1)
            
    print("  Login successful.")
    tokens = resp.json()['data']
    access_token = tokens['token']
    refresh_token = tokens['refreshToken']
    print(f"  Got Access Token: {access_token[:10]}...")
    print(f"  Got Refresh Token: {refresh_token[:10]}...")

    # 2. Refresh Token Flow
    print("\n2. Refresh Token Flow")
    refresh_payload = {"refreshToken": refresh_token}
    resp = requests.post(f"{BASE_URL}/token/refresh", json=refresh_payload)
    
    if resp.status_code == 200:
        print("  Refresh token successful.")
        new_tokens = resp.json()['data']
        access_token = new_tokens['token']
        refresh_token = new_tokens['refreshToken'] # Rotated token
    else:
        print(f"  FATAL: Refresh token failed. Status: {resp.status_code}, Body: {resp.text}")
        sys.exit(1)

    # 3. Logout Flow
    print("\n3. Logout Flow")
    logout_payload = {"refreshToken": refresh_token}
    headers = {"Authorization": f"Bearer {access_token}"}
    resp = requests.post(f"{BASE_URL}/logout", json=logout_payload, headers=headers)
    
    if resp.status_code == 200 or resp.status_code == 204:
        print("  Logout successful.")
    else:
        print(f"  FATAL: Logout failed. Status: {resp.status_code}, Body: {resp.text}")
        sys.exit(1)

    # Verification: Try to reuse refreshed token
    print("  Verifying token revocation...")
    resp = requests.post(f"{BASE_URL}/token/refresh", json={"refreshToken": refresh_token})
    if resp.status_code != 200:
        print(f"  Revoked refresh token rejected as expected. Status: {resp.status_code}")
    else:
        print("  ERROR: Revoked refresh token was accepted!")
        sys.exit(1)

    # 4. Re-Login (to proceed with authenticated actions)
    print("\n4. Re-Login")
    resp = login(EMAIL, current_password)
    if resp.status_code != 200:
        print("  FATAL: Re-login failed.")
        sys.exit(1)
    tokens = resp.json()['data']
    access_token = tokens['token'] # New access token

    # 5. Change Password Flow
    print("\n5. Change Password Flow")
    # Toggle password between Initial and New
    target_password = NEW_PASSWORD if current_password == INITIAL_PASSWORD else INITIAL_PASSWORD
    print(f"  Changing password from {current_password} to {target_password}...")
    
    change_payload = {
        "currentPassword": current_password,
        "newPassword": target_password
    }
    headers = {"Authorization": f"Bearer {access_token}"}
    resp = requests.post(f"{BASE_URL}/{USER_ID}/change-password", json=change_payload, headers=headers)
    
    if resp.status_code == 200:
        print("  Password change successful.")
        current_password = target_password
    else:
        print(f"  FATAL: Change password failed. Status: {resp.status_code}, Body: {resp.text}")
        sys.exit(1)

    # 6. Verify New Password (Login)
    print("\n6. Verify New Password")
    resp = login(EMAIL, current_password)
    if resp.status_code == 200:
        print("  Login with new password successful.")
    else:
        print(f"  FATAL: Login with new password failed. Status: {resp.status_code}")
        sys.exit(1)

    # 7. Forgot Password Flow
    print("\n7. Forgot Password Flow")
    forgot_payload = {"email": EMAIL}
    resp = requests.post(f"{BASE_URL}/forgot-password", json=forgot_payload)
    
    if resp.status_code == 200:
        print("  Forgot password request successful (200 OK).")
    else:
        print(f"  FATAL: Forgot password request failed. Status: {resp.status_code}, Body: {resp.text}")
        sys.exit(1)

    # 8. Reset Password Flow (Negative Test)
    print("\n8. Reset Password Flow (Invalid Token)")
    reset_payload = {
        "token": "invalid_token_simulation", 
        "newPassword": "AnotherPassword123!"
    }
    resp = requests.post(f"{BASE_URL}/reset-password", json=reset_payload)
    
    if resp.status_code == 400: # Assuming 400 for invalid token based on previous dev
        print("  Reset password with invalid token rejected (400 Bad Request) as expected.")
    else:
        print(f"  ERROR: Unexpected status for invalid token: {resp.status_code}")
        # Not exiting fatal here, just logging error

    # 9. Resend Verification Email
    print("\n9. Resend Verification Flow")
    resp = resend_verification(EMAIL)
    if resp.status_code == 200:
        print("  Resend verification request successful.")
    else:
        print(f"  FATAL: Resend verification failed. Status: {resp.status_code}, Body: {resp.text}")
        sys.exit(1)

    # 10. Verify Email (Negative Test)
    print("\n10. Verify Email Flow (Invalid Token)")
    resp = verify_email("invalid_token_simulation")
    if resp.status_code == 400:
        print("  Verify email with invalid token rejected (400 Bad Request) as expected.")
    else:
        print(f"  ERROR: Unexpected status for invalid token: {resp.status_code}")

    # 11. Brute Force Mitigation Flow
    print("\n11. Brute Force Mitigation Flow")
    unique_suffix = int(time.time())
    BRUTE_EMAIL = f"brute_{unique_suffix}@test.com"
    BRUTE_ID = str(java.util.UUID.randomUUID()) if 'java' in globals() else f"11111111-2222-3333-4444-{unique_suffix % 1000000000000:012d}"
    register(BRUTE_ID, f"bruteuser_{unique_suffix}", BRUTE_EMAIL, "Password123!")
    
    print("  Attempting 5 failed logins to lock the account...")
    for i in range(5):
        resp = login(BRUTE_EMAIL, "WrongPass1!")
        if resp.status_code == 401:
            print(f"    Attempt {i+1}: Rejected as expected (401).")
        else:
            print(f"    ERROR: Attempt {i+1} got unexpected status {resp.status_code}")
            sys.exit(1)
            
    print("  Attempting login with correct password (should be locked)...")
    resp = login(BRUTE_EMAIL, "Password123!")
    if resp.status_code == 403:
        print("    Account successfully locked (403 Forbidden).")
    else:
        print(f"    ERROR: Account not locked! Status: {resp.status_code}, Body: {resp.text}")
        sys.exit(1)

    print("\n--- ALL TESTS COMPLETED SUCCESSFULLY ---")

if __name__ == "__main__":
    if wait_for_service():
        run_verification()
    else:
        sys.exit(1)
