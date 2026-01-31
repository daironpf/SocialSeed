from typing import Protocol, Any, runtime_checkable

@runtime_checkable
class IServicePage(Protocol):
    """Protocol that every service-specific page object should implement."""
    base_url: str
    def setup(self) -> None: ...
    def teardown(self) -> None: ...

@runtime_checkable
class ITestModule(Protocol):
    """Protocol for test modules."""
    def run(self, context: Any) -> None: ...
