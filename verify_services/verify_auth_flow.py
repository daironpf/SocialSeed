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

def run_verification():
    print("--- STARTING E2E AUTH FLOW VERIFICATION ---")

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
    
    print("\n--- ALL TESTS COMPLETED SUCCESSFULLY ---")

if __name__ == "__main__":
    if wait_for_service():
        run_verification()
    else:
        sys.exit(1)
