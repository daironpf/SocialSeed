#!/usr/bin/env python3
"""
Quick E2E Test Runner - Runs all tests assuming services are already running
"""
import sys
import os
sys.path.insert(0, os.path.dirname(os.path.dirname(__file__)))

from verify_services.e2e.services.auth.auth_page import AuthPage
from verify_services.e2e.services.auth.modules._01_register_flow import run as register_flow
from verify_services.e2e.services.auth.modules._02_login_flow import run as login_flow
from verify_services.e2e.services.auth.modules._06_refresh_flow import run as refresh_flow
from verify_services.e2e.services.auth.modules._99_logout_flow import run as logout_flow
from verify_services.e2e.services.auth.modules._04_change_password_flow import run as change_password_flow
from verify_services.e2e.services.auth.modules._10_forgot_password_flow import run as forgot_password_flow
from verify_services.e2e.services.auth.modules._11_reset_password_flow import run as reset_password_flow
from verify_services.e2e.services.auth.modules._12_verify_email_flow import run as verify_email_flow
from verify_services.e2e.services.auth.modules._05_change_username_flow import run as change_username_flow
from verify_services.e2e.services.auth.modules._03_change_email_flow import run as change_email_flow
from verify_services.e2e.services.auth.modules._13_resend_verification_flow import run as resend_verification_flow
from verify_services.e2e.services.auth.modules._20_credential_expiration_flow import run as credential_expiration_flow

print("=== SocialSeed E2E Test Suite ===")
print("Assuming services are already running on Docker...")
print()

# Check services
try:
    import requests
    auth_health = requests.get("http://localhost:8085/auth/actuator/health", timeout=5)
    print(f"✓ auth-service: {auth_health.json()['status']}")
except Exception as e:
    print(f"✗ auth-service not accessible: {e}")
    sys.exit(1)

try:
    socialuser_health = requests.get("http://localhost:8090/actuator/health", timeout=5)
    print(f"✓ socialuser-service: {socialuser_health.json()['status']}")
except Exception as e:
    print(f"⚠ socialuser-service not accessible: {e}")

print("\n=== Running Auth Service E2E Tests ===\n")

page = AuthPage()
page.setup()
success = True
passed = 0
failed = 0
tests = [
    ("Registration Flow", register_flow),
    ("Login Flow", login_flow),
    ("Refresh Token Flow", refresh_flow),
    ("Logout Flow", logout_flow),
    ("Re-Login", login_flow),
    ("Change Password Flow", change_password_flow),
    ("Verify New Password", login_flow),
    ("Forgot Password Flow", forgot_password_flow),
    ("Reset Password Flow (Negative)", reset_password_flow),
    ("Verify Email Flow", verify_email_flow),
    ("Re-Login for Username Change", login_flow),
    ("Change Username Flow", change_username_flow),
    ("Re-Login for Email Change", login_flow),
    ("Change Email Flow", change_email_flow),
    ("Resend Verification Flow", resend_verification_flow),
    ("Credential Expiration Flow", credential_expiration_flow),
]

for test_name, test_func in tests:
    print(f"\n--- {test_name} ---")
    try:
        test_func(page)
        print(f"✓ {test_name} passed")
        passed += 1
    except Exception as e:
        print(f"✗ {test_name} failed: {e}")
        failed += 1
        success = False

page.teardown()

print("\n" + "="*50)
print(f"Total: {passed + failed} tests")
print(f"Passed: {passed}")
print(f"Failed: {failed}")

if success:
    print("\n🎉 All E2E tests passed!")
    sys.exit(0)
else:
    print("\n❌ Some E2E tests failed!")
    sys.exit(1)
