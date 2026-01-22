import time
import requests
import sys

BASE_URL = "http://localhost:8081/auth"
ACTUATOR_URL = "http://localhost:8081/actuator/health"

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

def run_verification():
    # 1. Register (SKIPPED due to gRPC dependency)
    # Using manually inserted user
    user_id = "123e4567-e89b-12d3-a456-426614178888"
    email = "user@strong.com"
    username = "userstrong"
    password = "StrongPass1!" 
    
    print(f"\n1. Skipping registration (User manually inserted)...")
    
    # 2. Login
    print(f"\n2. Logging in with initial password...")
    login_payload = {"email": email, "password": password}
    resp = requests.post(f"{BASE_URL}/login", json=login_payload)
    
    if resp.status_code != 200:
        print(f"Login failed: {resp.text}")
        # If login fails, maybe password was already changed? Try new password
        login_payload["password"] = "NewSecretPassword123!"
        print("Trying new password...")
        resp = requests.post(f"{BASE_URL}/login", json=login_payload)
        if resp.status_code != 200:
            print("Login failed with new password too. Exiting.")
            sys.exit(1)
        else:
            print("Login succeeded with NEW password. Resetting for test...")
            # We need to be in 'initial' state to test change properly? 
            # Or we just proceed to change it BACK or something.
            # Let's just proceed assuming we want to change FROM current (which is New) TO something else.
            password = "NewSecretPassword123!"

    tokens = resp.json()['data']
    access_token = tokens['token']
    refresh_token = tokens['refreshToken']
    print(f"Login successful. Got tokens.")

    # 3. Change Password
    new_password = "NewSecretPassword123!" if password != "NewSecretPassword123!" else "InitialPassword123!"
    print(f"\n3. Changing password to {new_password}...")
    
    change_payload = {
        "currentPassword": password,
        "newPassword": new_password
    }
    headers = {"Authorization": f"Bearer {access_token}"}
    
    resp = requests.post(f"{BASE_URL}/{user_id}/change-password", json=change_payload, headers=headers)
    
    if resp.status_code == 200:
        print("Password change successful.")
    else:
        print(f"Password change failed: {resp.status_code} - {resp.text}")
        sys.exit(1)

    # 4. Verify Refresh Token (should fail)
    print("\n4. Verifying old Refresh Token (should fail)...")
    refresh_payload = {"refreshToken": refresh_token}
    resp = requests.post(f"{BASE_URL}/token/refresh", json=refresh_payload)
    
    # Expect failure (401 or 403 or 404 depending on impl)
    # AuthServiceImpl.refreshToken checks db. If deleted, it returns ...?
    # Actually finding by token: if not found -> empty -> throws exception or returns null?
    # Service: findByToken(token).map...
    # Integration test expected: ErrorCode.REFRESH_TOKEN_INVALID_EXPIRED (or similar) which maps to ... 401/403?
    
    print(f"Refresh response: {resp.status_code}")
    if resp.status_code != 200:
        print("Old refresh token rejected as expected.")
    else:
        print("ERROR: Old refresh token still valid!")
        sys.exit(1)

    # 5. Login with New Password
    print(f"\n5. Logging in with NEW password...")
    login_payload = {"email": email, "password": new_password}
    resp = requests.post(f"{BASE_URL}/login", json=login_payload)
    
    if resp.status_code == 200:
        print("Login with new password successful.")
    else:
        print(f"Login with new password failed: {resp.text}")
        sys.exit(1)

    print("\n\nVERIFICATION PASSED!")

if __name__ == "__main__":
    if wait_for_service():
        run_verification()
    else:
        sys.exit(1)
