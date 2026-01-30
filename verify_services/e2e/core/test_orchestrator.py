import pkgutil
import importlib
from typing import List, Dict, Any, Callable
from pathlib import Path

try:
    import pytest
except ImportError:
    pytest = None


class TestOrchestrator:
    """
    Orchestrates dynamic loading and execution of test modules.
    Auto-discovers modules in services/*/modules/ and runs them.
    """
    def __init__(self, services_dir: str = "verify_services/e2e/services"):
        self.services_dir = Path(services_dir)
        self.modules: Dict[str, List[Callable]] = {}

    def discover_modules(self):
        """Discover all test modules in services directories."""
        for service_dir in self.services_dir.iterdir():
            if service_dir.is_dir():
                modules_dir = service_dir / "modules"
                if modules_dir.exists():
                    service_name = service_dir.name
                    self.modules[service_name] = []
                    for module_info in pkgutil.iter_modules([str(modules_dir)]):
                        module = importlib.import_module(f"verify_services.e2e.services.{service_name}.modules.{module_info.name}")
                        # Assume each module has a 'run' function or class
                        if hasattr(module, 'run'):
                            self.modules[service_name].append(module.run)

    def run_service_tests(self, service_name: str, context: Any):
        """Run all modules for a specific service."""
        if service_name not in self.modules:
            raise ValueError(f"Service {service_name} not found or has no modules.")
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
        config.orchestrator = TestOrchestrator()

def pytest_collection_modifyitems(config, items):
    """Modify test collection to include dynamic modules."""
    if pytest:
        orchestrator = getattr(config, 'orchestrator', None)
        if orchestrator:
            orchestrator.discover_modules()