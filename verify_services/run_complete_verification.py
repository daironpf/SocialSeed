#!/usr/bin/env python3
"""
Complete E2E Verification Runner
Executes all E2E tests for all services in logical order.
Verifies if services are running, and starts them with dev profile if needed.
"""
import sys
import os
import subprocess
import time
sys.path.insert(0, os.path.dirname(os.path.dirname(__file__)))

import requests
from typing import Optional, Dict, Any, List, Tuple, Callable

from verify_services.e2e.services.auth.auth_page import AuthPage

# Import auth modules directly to control execution order
from verify_services.e2e.services.auth.modules import (
    register_flow,
    login_flow,
    refresh_flow,
    logout_flow,
    change_password_flow,
    forgot_password_flow,
    reset_password_flow,
    verify_email_flow,
    change_username_flow,
    change_email_flow,
    resend_verification_flow,
    credential_expiration_flow
)

# Type alias for module function
ModuleFunc = Callable[[AuthPage], Any]


class ServiceManager:
    """Manages service startup and health checks."""

    def __init__(self) -> None:
        self.processes: Dict[str, subprocess.Popen] = {}

    def check_service_health(self, url: str) -> bool:
        """Check if a service is healthy."""
        try:
            response = requests.get(url, timeout=5)
            return response.status_code == 200
        except Exception:
            return False

    def wait_for_service(self, url: str, service_name: str, timeout: int = 120) -> bool:
        """Wait for a service to be healthy."""
        print(f"Waiting for {service_name} to be up...")
        start_time = time.time()
        while time.time() - start_time < timeout:
            if self.check_service_health(url):
                print(f"✓ {service_name} is UP!")
                return True
            time.sleep(2)
        print(f"✗ {service_name} failed to start within {timeout} seconds.")
        return False

    def start_auth_service(self) -> bool:
        """Start auth-service with dev profile if not running."""
        auth_url = "http://localhost:8085/actuator/health"

        if self.check_service_health(auth_url):
            print("✓ auth-service is already running")
            return True

        print("Starting auth-service with dev profile...")
        try:
            cmd = [
                "mvn", "spring-boot:run",
                "-pl", "services/auth-service",
                "-Dspring-boot.run.profiles=dev",
                "-Dspring-boot.run.jvmArguments=-Dserver.port=8085"
            ]
            process = subprocess.Popen(
                cmd,
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
                cwd="/home/dairon/proyectos/SocialSeed"
            )
            self.processes["auth"] = process
            return self.wait_for_service(auth_url, "auth-service")
        except Exception as e:
            print(f"✗ Failed to start auth-service: {e}")
            return False

    def start_socialuser_service(self) -> bool:
        """Start socialuser-service with dev profile if not running."""
        social_url = "http://localhost:8090/actuator/health"

        if self.check_service_health(social_url):
            print("✓ socialuser-service is already running")
            return True

        print("Starting socialuser-service with dev profile (AuraDB)...")
        try:
            cmd = [
                "mvn", "spring-boot:run",
                "-pl", "services/socialuser-service",
                "-Dspring-boot.run.profiles=dev",
                "-Dspring-boot.run.jvmArguments=-Dserver.port=8090"
            ]
            process = subprocess.Popen(
                cmd,
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
                cwd="/home/dairon/proyectos/SocialSeed"
            )
            self.processes["socialuser"] = process
            return self.wait_for_service(social_url, "socialuser-service")
        except Exception as e:
            print(f"⚠ Failed to start socialuser-service: {e} (may not be required for current tests)")
            return True  # Don't fail if socialuser is not required

    def cleanup(self) -> None:
        """Stop all started services."""
        print("\nCleaning up services...")
        for name, process in self.processes.items():
            try:
                print(f"Stopping {name} service...")
                process.terminate()
                process.wait(timeout=10)
                print(f"✓ {name} service stopped")
            except Exception as e:
                print(f"⚠ Could not stop {name} service: {e}")


def create_auth_context() -> AuthPage:
    """Create auth page context."""
    page = AuthPage()
    page.setup()
    return page


def main() -> int:
    print("=== Starting E2E Verification for SocialSeed ===")

    service_manager = ServiceManager()

    # Start required services
    if not service_manager.start_auth_service():
        print("✗ Cannot proceed without auth-service")
        return 1

    # Optionally start socialuser-service (may not be required for auth tests)
    socialuser_started = service_manager.start_socialuser_service()

    auth_page = create_auth_context()
    success = True

    try:
        print("\n=== Running Auth Service E2E Tests ===")

        # Module execution order based on original script
        modules: List[Tuple[str, ModuleFunc]] = [
            ("Registration Flow", register_flow.run),
            ("Login Flow", login_flow.run),
            ("Refresh Token Flow", refresh_flow.run),
            ("Logout Flow", logout_flow.run),
            ("Re-Login", login_flow.run),
            ("Change Password Flow", change_password_flow.run),
            ("Verify New Password", login_flow.run),
            ("Forgot Password Flow", forgot_password_flow.run),
            ("Reset Password Flow (Negative)", reset_password_flow.run),
            ("Verify Email Flow", verify_email_flow.run),
            ("Re-Login for Username Change", login_flow.run),
            ("Change Username Flow", change_username_flow.run),
            ("Re-Login for Email Change", login_flow.run),
            ("Change Email Flow", change_email_flow.run),
            ("Resend Verification Flow", resend_verification_flow.run),
            ("Credential Expiration Flow", credential_expiration_flow.run)
        ]

        for module_name, module_func in modules:
            print(f"\n--- {module_name} ---")
            try:
                module_func(auth_page)
                print(f"✓ {module_name} passed")
            except Exception as e:
                print(f"✗ {module_name} failed: {e}")
                import traceback
                traceback.print_exc()
                success = False
                break

        if success:
            print("\n🎉 All Auth E2E tests passed!")
        else:
            print("\n❌ Some Auth E2E tests failed!")

    finally:
        auth_page.teardown()
        service_manager.cleanup()

    return 0 if success else 1


if __name__ == "__main__":
    exit(main())
