from playwright.sync_api import APIRequestContext, Playwright, APIResponse
from typing import Optional, Dict, Any, Any as AnyType
from .headers import DEFAULT_JSON_HEADERS, DEFAULT_BROWSER_HEADERS
from .models import ServiceConfig


class BasePage:
    """
    Base class for API testing using Playwright's APIRequestContext.
    Provides common HTTP methods and utilities.
    """
    def __init__(
        self, 
        base_url: str, 
        playwright: Optional[Playwright] = None,
        default_headers: Optional[Dict[str, str]] = None
    ) -> None:
        self.base_url: str = base_url.rstrip('/')
        self.playwright_manager: Optional[AnyType] = None
        self.playwright: Optional[Playwright] = None
        self.default_headers = default_headers if default_headers is not None else {
            **DEFAULT_JSON_HEADERS,
            **DEFAULT_BROWSER_HEADERS
        }
        
        if playwright:
            self.playwright = playwright
        else:
            self.playwright_manager = __import__('playwright').sync_api.sync_playwright()
            self.playwright = self.playwright_manager.__enter__()
        
        self.api_context: Optional[APIRequestContext] = None

    @classmethod
    def from_config(cls, config: ServiceConfig, playwright: Optional[Playwright] = None) -> 'BasePage':
        """Factory method to create a BasePage from a ServiceConfig object."""
        return cls(
            base_url=config.base_url,
            playwright=playwright,
            default_headers=config.default_headers or None
        )

    def setup(self) -> None:
        """Initialize the API context."""
        if not self.api_context:
            self.api_context = self.playwright.request.new_context()

    def teardown(self) -> None:
        """Clean up the API context."""
        if self.api_context:
            self.api_context.dispose()
            self.api_context = None
        if self.playwright_manager:
            self.playwright_manager.__exit__(None, None, None)
            self.playwright_manager = None

    def _prepare_headers(self, headers: Optional[Dict[str, str]]) -> Dict[str, str]:
        """Combine default headers with request-specific headers."""
        request_headers = self.default_headers.copy()
        if headers:
            request_headers.update(headers)
        return request_headers

    def get(self, endpoint: str, headers: Optional[Dict[str, str]] = None, params: Optional[Dict[str, Any]] = None) -> APIResponse:
        """Perform GET request."""
        self._ensure_setup()
        full_url = f"{self.base_url}{endpoint}"
        return self.api_context.get(full_url, headers=self._prepare_headers(headers), params=params)

    def post(self, endpoint: str, data: Optional[Dict[str, Any]] = None, headers: Optional[Dict[str, str]] = None) -> APIResponse:
        """Perform POST request."""
        self._ensure_setup()
        full_url = f"{self.base_url}{endpoint}"
        return self.api_context.post(full_url, data=data, headers=self._prepare_headers(headers))

    def put(self, endpoint: str, data: Optional[Dict[str, Any]] = None, headers: Optional[Dict[str, str]] = None) -> APIResponse:
        """Perform PUT request."""
        self._ensure_setup()
        full_url = f"{self.base_url}{endpoint}"
        return self.api_context.put(full_url, data=data, headers=self._prepare_headers(headers))

    def delete(self, endpoint: str, headers: Optional[Dict[str, str]] = None) -> APIResponse:
        """Perform DELETE request."""
        self._ensure_setup()
        full_url = f"{self.base_url}{endpoint}"
        return self.api_context.delete(full_url, headers=self._prepare_headers(headers))

    def patch(self, endpoint: str, data: Optional[Dict[str, Any]] = None, headers: Optional[Dict[str, str]] = None) -> APIResponse:
        """Perform PATCH request."""
        self._ensure_setup()
        full_url = f"{self.base_url}{endpoint}"
        return self.api_context.patch(full_url, data=data, headers=self._prepare_headers(headers))

    def _ensure_setup(self) -> None:
        """Ensure API context is set up."""
        if not self.api_context:
            self.setup()

    @staticmethod
    def get_response_text(response: APIResponse) -> str:
        """Get response text from Playwright APIResponse."""
        return response.body().decode('utf-8')