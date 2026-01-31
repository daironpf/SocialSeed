# E2E Core API Testing Engine

This is a service-agnostic E2E API testing engine designed to be highly decoupled, extensible, and AI-agent friendly.

## Features

- **Service-Agnostic**: No hardcoded dependencies on specific services or domains.
- **Dynamic Orchestration**: Automatically discovers and runs test modules based on directory structure.
- **Environment Injection**: Configuration-driven approach using environment variables and `.env` files.
- **Type-Safe**: Uses Python type hints and Pydantic for configuration validation.
- **Structural Typing**: Uses `typing.Protocol` to define interfaces, ensuring low coupling.

## Installation

To use this engine in a new project, copy the `core/` folder to your project's E2E directory.

```bash
cp -r verify_services/e2e/core /your/new/project/e2e/
```

Dependencies required:
- `playwright`
- `pydantic`
- `python-dotenv`
- `pytest` (optional, for pytest integration)

## Configuration

The engine uses environment variables for configuration. Example `.env`:

```env
E2E_ROOT_DIR=/absolute/path/to/project
AUTH_BASE_URL=http://localhost:8085/auth
AUTH_TIMEOUT=5000
E2E_USER_AGENT=Custom-Agent/1.0
```

## Basic Usage

### 1. Define a Service Page

```python
from core.base_page import BasePage
from core.models import ServiceConfig

class MyServicePage(BasePage):
    def login(self, username, password):
        return self.post("/login", data={"user": username, "pass": password})
```

### 2. Create a Test Module

Create a file in your service's `modules/` directory:

```python
# my_service/modules/login_flow.py

def run(context):
    page = context.get_service_page("my_service")
    response = page.login("admin", "admin")
    assert response.status == 200
```

### 3. Run with Orchestrator

```python
from core.test_orchestrator import TestOrchestrator
from core.config import ConfigLoader

# Initialize orchestrator
orchestrator = TestOrchestrator(services_path="path/to/services")
orchestrator.discover_modules()

# Run tests
orchestrator.run_all_tests(context_factory=my_custom_context_factory)
```

## AI Governance

This framework adheres to the rules defined in `.agent/rules.yaml`. AI agents should follow the protocol in `.agent/workflow.md` when generating new tests.
