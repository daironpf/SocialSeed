from typing import Dict

DEFAULT_JSON_HEADERS: Dict[str, str] = {
    "Content-Type": "application/json",
    "Accept": "application/json, text/plain, */*"
}

DEFAULT_BROWSER_HEADERS: Dict[str, str] = {
    "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
    "Accept-Language": "en-US,en;q=0.9",
    "Connection": "keep-alive"
}

def get_combined_headers(custom_headers: Dict[str, str] = None) -> Dict[str, str]:
    """Helper to combine default headers with custom ones."""
    headers = {**DEFAULT_JSON_HEADERS, **DEFAULT_BROWSER_HEADERS}
    if custom_headers:
        headers.update(custom_headers)
    return headers
