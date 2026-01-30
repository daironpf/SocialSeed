#!/usr/bin/env python3
"""
Simple runner for auth E2E tests using the modular framework.
"""
from verify_services.e2e.core.test_orchestrator import TestOrchestrator
from verify_services.e2e.services.auth.auth_page import AuthPage

def main():
    orchestrator = TestOrchestrator()
    orchestrator.discover_modules()

    def context_factory():
        page = AuthPage()
        page.setup()
        return page

    try:
        orchestrator.run_all_tests(context_factory)
        print("All tests passed!")
    except Exception as e:
        print(f"Tests failed: {e}")
        exit(1)

if __name__ == "__main__":
    main()