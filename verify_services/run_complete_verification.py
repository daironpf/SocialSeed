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
from pathlib import Path
sys.path.insert(0, os.path.dirname(os.path.dirname(__file__)))

import requests
from typing import Optional, Dict, Any, List, Tuple, Callable

from verify_services.e2e.services.auth.auth_page import AuthPage
from verify_services.e2e.core.config_loader import ApiConfigLoader
from verify_services.e2e.core.loaders import ModuleLoader

# Type alias for module function
ModuleFunc = Callable[[AuthPage], Any]

# Load test modules dynamically
_module_loader = ModuleLoader()
_modules_path = Path(__file__).parent / "e2e" / "services" / "auth" / "modules"
_loaded_modules = _module_loader.discover_runnables(_modules_path)

# Create a dictionary for easy access by module name
_module_map = {}
for mod in _loaded_modules:
    # Get the module name from the function's module
    mod_name = mod.__module__.split('.')[-1] if hasattr(mod, '__module__') else str(mod)
    _module_map[mod_name] = mod

# Export commonly used modules
register_flow = _module_map.get('01_register_flow')
login_flow = _module_map.get('02_login_flow')
refresh_flow = _module_map.get('06_refresh_flow')
logout_flow = _module_map.get('99_logout_flow')
change_password_flow = _module_map.get('04_change_password_flow')
forgot_password_flow = _module_map.get('10_forgot_password_flow')
reset_password_flow = _module_map.get('11_reset_password_flow')
verify_email_flow = _module_map.get('12_verify_email_flow')
change_username_flow = _module_map.get('05_change_username_flow')
change_email_flow = _module_map.get('03_change_email_flow')
resend_verification_flow = _module_map.get('13_resend_verification_flow')
credential_expiration_flow = _module_map.get('20_credential_expiration_flow')


class ServiceManager:
    """Manages service startup and health checks using api.conf configuration."""

    def __init__(self) -> None:
        self.processes: Dict[str, subprocess.Popen] = {}
        # Import here to avoid circular imports
        from verify_services.e2e.core.config_loader import ApiConfigLoader
        self.config = ApiConfigLoader.load()

    def check_service_health(self, url: str) -> bool:
        """Check if a service is healthy."""
        try:
            verify_ssl = self.config.security.verify_ssl
            response = requests.get(url, timeout=5, verify=verify_ssl)
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

    def start_service(self, service_name: str) -> bool:
        """
        Start a service using configuration from api.conf.
        
        Args:
            service_name: Name of the service as defined in api.conf
            
        Returns:
            bool: True if service is running or started successfully
        """
        service_config = self.config.services.get(service_name)
        if not service_config:
            print(f"⚠ Service '{service_name}' not found in api.conf configuration")
            return False

        # Build health check URL
        base_url = service_config.base_url.rstrip('/')
        health_endpoint = service_config.health_endpoint
        health_url = f"{base_url}{health_endpoint}"

        if self.check_service_health(health_url):
            print(f"✓ {service_config.name} is already running")
            return True

        # Check if auto_start is enabled
        if not service_config.auto_start:
            if service_config.required:
                print(f"✗ {service_config.name} is required but not running and auto_start is disabled")
                return False
            else:
                print(f"⚠ {service_config.name} is not running but not required - continuing")
                return True

        print(f"Starting {service_config.name} with dev profile...")
        try:
            cmd = [
                "mvn", "spring-boot:run",
                "-pl", service_config.maven_module,
                "-Dspring-boot.run.profiles=dev",
                f"-Dspring-boot.run.jvmArguments=-Dserver.port={service_config.port}"
            ]
            
            # Determine project root from config path or use default
            config_path = ApiConfigLoader.get_config_path()
            if config_path:
                project_root = config_path.parent.parent
            else:
                # Fallback to current working directory
                project_root = Path.cwd()
            
            process = subprocess.Popen(
                cmd,
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
                cwd=str(project_root)
            )
            self.processes[service_name] = process
            return self.wait_for_service(health_url, service_config.name)
        except Exception as e:
            print(f"✗ Failed to start {service_config.name}: {e}")
            if service_config.required:
                return False
            return True  # Don't fail if not required

    def start_auth_service(self) -> bool:
        """Start auth-service with dev profile if not running."""
        return self.start_service("auth")

    def start_socialuser_service(self) -> bool:
        """Start socialuser-service with dev profile if not running."""
        return self.start_service("socialuser")

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
        # Modules are already the run functions loaded dynamically
        modules: List[Tuple[str, Optional[ModuleFunc]]] = [
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
            ("Credential Expiration Flow", credential_expiration_flow)
        ]

        for module_name, module_func in modules:
            print(f"\n--- {module_name} ---")
            if module_func is None:
                print(f"⚠ {module_name} module not found, skipping")
                continue
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
