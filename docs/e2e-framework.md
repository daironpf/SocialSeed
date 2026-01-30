# E2E Testing Framework Documentation

## Overview
The E2E testing framework has been refactored from linear scripts to a modular component-based architecture using Playwright for API testing. This prepares for future UI testing while maintaining API coverage.

## Structure
```
verify_services/e2e/
├── core/
│   ├── base_page.py        # Low-level Playwright API wrappers
│   └── test_orchestrator.py # Auto-loads and runs test modules
├── services/
│   ├── auth/
│   │   ├── auth_page.py     # Hub for auth state and data
│   │   ├── data_schema.py   # DTOs and constants
│   │   └── modules/         # Individual test flows
│   │       ├── login_flow.py
│   │       ├── register_flow.py
│   │       └── refresh_flow.py
│   └── [other services]/
└── run_auth_tests.py       # Example runner
```

## How to Run Tests
1. Install dependencies: `pip install -r requirements.txt`
2. Run auth tests: `python verify_services/e2e/run_auth_tests.py`
3. Run complete verification: `./verify_services/run_complete_verification.py` (runs all services in logical order)

## Adding New Modules
1. Create a new `.py` file in `services/[service]/modules/`
2. Define a `run` function that takes the service page instance
3. The orchestrator will auto-discover and run it

## Adding New Services
1. Create `services/[new_service]/` with `page.py`, `data_schema.py`, `modules/`
2. Follow the auth service pattern
3. No core changes needed

## Success Criteria
- Modules run independently or chained
- Shared state persists between modules
- Easy to add new services/modules without touching core