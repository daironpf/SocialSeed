from playwright.sync_api import APIRequestContext, Playwright, APIResponse
from typing import Optional, Dict, Any, Any as AnyType
import json


class BasePage:
    """
    Base class for API testing using Playwright's APIRequestContext.
    Provides common HTTP methods and utilities.
    """
    def __init__(self, base_url: str, playwright: Optional[Playwright] = None) -> None:
        self.base_url: str = base_url.rstrip('/')
        self.playwright_manager: Optional[AnyType] = None
        self.playwright: Optional[Playwright] = None
        if playwright:
            self.playwright = playwright
        else:
            self.playwright_manager = __import__('playwright').sync_api.sync_playwright()
            self.playwright = self.playwright_manager.__enter__()
        # Create context without base_url to avoid issues with Playwright
        self.api_context: Optional[APIRequestContext] = None

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

    def get(self, endpoint: str, headers: Optional[Dict[str, str]] = None, params: Optional[Dict[str, Any]] = None) -> APIResponse:
        """Perform GET request."""
        self._ensure_setup()
        # Build full URL
        full_url = f"{self.base_url}{endpoint}"
        return self.api_context.get(full_url, headers=headers, params=params)

    def post(self, endpoint: str, data: Optional[Dict[str, Any]] = None, headers: Optional[Dict[str, str]] = None) -> APIResponse:
        """Perform POST request."""
        self._ensure_setup()
        # Build full URL
        full_url = f"{self.base_url}{endpoint}"
        # Set default headers for JSON and common browser headers
        request_headers = headers or {}
        request_headers = {
            **request_headers,
            'Content-Type': 'application/json',
            'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
            'Accept': 'application/json, text/plain, */*',
        }
        # Pass Python object directly, Playwright will serialize it
        return self.api_context.post(full_url, data=data, headers=request_headers)

    def put(self, endpoint: str, data: Optional[Dict[str, Any]] = None, headers: Optional[Dict[str, str]] = None) -> APIResponse:
        """Perform PUT request."""
        self._ensure_setup()
        # Build full URL
        full_url = f"{self.base_url}{endpoint}"
        # Set default headers for JSON
        request_headers = headers or {}
        request_headers = {
            **request_headers,
            'Content-Type': 'application/json',
            'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
            'Accept': 'application/json, text/plain, */*',
        }
        # Pass Python object directly, Playwright will serialize it
        return self.api_context.put(full_url, data=data, headers=request_headers)

    def delete(self, endpoint: str, headers: Optional[Dict[str, str]] = None) -> APIResponse:
        """Perform DELETE request."""
        self._ensure_setup()
        # Build full URL
        full_url = f"{self.base_url}{endpoint}"
        request_headers = headers or {}
        request_headers = {
            **request_headers,
            'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
            'Accept': 'application/json, text/plain, */*',
        }
        return self.api_context.delete(full_url, headers=request_headers)

    def patch(self, endpoint: str, data: Optional[Dict[str, Any]] = None, headers: Optional[Dict[str, str]] = None) -> APIResponse:
        """Perform PATCH request."""
        self._ensure_setup()
        # Build full URL
        full_url = f"{self.base_url}{endpoint}"
        # Set default headers for JSON
        request_headers = headers or {}
        request_headers = {
            **request_headers,
            'Content-Type': 'application/json',
            'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
            'Accept': 'application/json, text/plain, */*',
        }
        # Pass Python object directly, Playwright will serialize it
        return self.api_context.patch(full_url, data=data, headers=request_headers)

    def _ensure_setup(self) -> None:
        """Ensure API context is set up."""
        if not self.api_context:
            self.setup()

    @staticmethod
    def get_response_text(response: APIResponse) -> str:
        """Get response text from Playwright APIResponse."""
        return response.body().decode('utf-8')