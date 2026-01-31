import importlib.util
import os
import sys
from typing import List, Dict, Any, Callable, Optional
from pathlib import Path

try:
    import pytest
except ImportError:
    pytest = None


from .loaders import ModuleLoader

class TestOrchestrator:
    """
    Orchestrates dynamic loading and execution of test modules.
    Auto-discovers modules in services/*/modules/ and runs them.
    """
    def __init__(
        self, 
        root_dir: Optional[str] = None,
        services_path: str = "verify_services/e2e/services"
    ):
        # Default to the directory containing the 'verify_services' folder or current working directory
        self.root_dir = Path(root_dir) if root_dir else Path(os.getcwd())
        self.services_dir = self.root_dir / services_path
        self.loader = ModuleLoader()
        self.modules: Dict[str, List[Callable]] = {}

    def discover_modules(self):
        """Discover all test modules in services directories using dynamic file loading."""
        if not self.services_dir.exists():
            print(f"Warning: Services directory not found at {self.services_dir}")
            return

        for service_dir in self.services_dir.iterdir():
            if service_dir.is_dir():
                modules_dir = service_dir / "modules"
                if modules_dir.exists():
                    service_name = service_dir.name
                    self.modules[service_name] = self.loader.discover_runnables(modules_dir)


    def run_service_tests(self, service_name: str, context: Any):
        """Run all modules for a specific service."""
        if service_name not in self.modules:
            raise ValueError(f"Service {service_name} not found or has no modules discovered at {self.services_dir}")
        for run_func in self.modules[service_name]:
            run_func(context)

    def run_all_tests(self, context_factory: Callable[[], Any]):
        """Run all discovered tests with a context factory."""
        for service_name, modules in self.modules.items():
            context = context_factory()
            try:
                for run_func in modules:
                    run_func(context)
                print(f"✓ {service_name} tests passed")
            except Exception as e:
                print(f"✗ {service_name} tests failed: {e}")
                raise
            finally:
                if hasattr(context, 'teardown'):
                    context.teardown()


# Pytest integration
def pytest_configure(config):
    """Hook to set up orchestrator for pytest."""
    if pytest:
        # Configuration can be passed via environment variables
        root = os.getenv("E2E_ROOT_DIR")
        spath = os.getenv("E2E_SERVICES_PATH", "verify_services/e2e/services")
        config.orchestrator = TestOrchestrator(root_dir=root, services_path=spath)

def pytest_collection_modifyitems(config, items):
    """Modify test collection to include dynamic modules."""
    if pytest:
        orchestrator = getattr(config, 'orchestrator', None)
        if orchestrator:
            orchestrator.discover_modules()