# Task.md: Refactor E2E Framework to Modular Component-Based Architecture

## Overview
Refactor the E2E testing framework from linear scripts to a modular Playwright-based architecture. Since no frontend exists yet (future Flutter app), adapt the approach for API testing with Playwright's request context instead of browser UI. This allows testing backend APIs in a modular way, preparing for future UI integration.

## Assumptions and Decisions
- **Framework Shift**: Use Playwright for HTTP requests (via APIContext) instead of `requests` for better async support and future UI readiness.
- **Location**: Place the framework in `verify_services/e2e/` to keep with existing scripts.
- **Dynamic Loading**: Use `pkgutil` for auto-loading modules.
- **Data Sharing**: Service hubs manage state and DTOs.
- **Testing**: Modules run independently or orchestrated via pytest.

## Phases and Tasks

### Phase 1: Setup and Core Infrastructure
1. **Create Directory Structure** ✓
    - Created `verify_services/e2e/core/` and `verify_services/e2e/services/auth/modules/`.

2. **Install Dependencies** ✓
    - Added to `requirements.txt`: `playwright`, `pytest`, `pytest-asyncio`, `pydantic`.

3. **Implement Core Components** ✓
    - `verify_services/e2e/core/base_page.py`: Base class with strong typing for API interactions using Playwright APIContext.
    - `verify_services/e2e/core/test_orchestrator.py`: Dynamic loader for modules.

### Phase 2: Auth Service Modularization
4. **Create Auth Hub and Schema** ✓
    - `verify_services/e2e/services/auth/auth_page.py`: Hub with all CRUD methods and strong typing.
    - `verify_services/e2e/services/auth/data_schema.py`: Pydantic DTOs for auth data.

5. **Break Down Existing Logic into Modules** ✓
    - Created all modules: `register_flow.py`, `login_flow.py`, `refresh_flow.py`, `logout_flow.py`, `change_password_flow.py`, `forgot_password_flow.py`, `reset_password_flow.py`, `verify_email_flow.py`, `change_username_flow.py`, `change_email_flow.py`, `resend_verification_flow.py`, `credential_expiration_flow.py`.
    - Adapted API calls to Playwright requests with proper headers.

6. **Migrate and Adapt Scripts** ✓
    - Migrated all logic from `verify_auth_flow.py` to modular structure.

### Phase 3: Extend and Automation
7. **Add SocialUser Service** (Optional - not implemented yet)
    - Mirror structure for socialuser.

8. **Implement Dynamic Loading** ✓
    - Updated orchestrator for auto-discovery.

9. **AI Automation Rules** ✓
    - All modules use strong typing with `TYPE_CHECKING`.

### Phase 4: Testing and Documentation
10. **Run and Validate** ✓
    - `verify_services/run_complete_verification.py`: Full service management with auto-startup.

11. **Update Documentation** ✓
    - Created `docs/e2e-framework.md`.

### Phase 5: Cleanup
12. **Deprecate Old Scripts** ✓
    - Archived `verify_services/verify_auth_flow.py` to `verify_services/archived/verify_auth_flow.py`.

13. **Final Review** ✓
    - All tests passed successfully.
    - Service management works correctly (auto-start/stop).

## Success Criteria
- [x] Modular architecture with Playwright APIContext
- [x] Strong typing throughout
- [x] All Auth Service flows tested and passing
- [x] Auto-service management with dev profile support
- [x] SocialUser service integration (AuraDB) verified
- [x] Old script archived
