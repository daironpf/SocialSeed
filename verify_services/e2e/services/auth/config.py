"""Auth service configuration using api.conf.

This module provides Auth-specific configuration using the centralized
api.conf configuration file with fallback to legacy environment variables.
"""

import os
from typing import Dict
from ...core.config_loader import ApiConfigLoader, ServiceConfig
from ...core.models import ServiceConfig as OldServiceConfig


def get_auth_config() -> OldServiceConfig:
    """
    Gets the configuration for the Auth service.
    
    Priority order:
    1. Load from api.conf using ApiConfigLoader
    2. Fallback to environment variables
    3. Use sensible defaults for local development
    
    Returns:
        ServiceConfig: Configuration for the Auth service
    """
    try:
        # Try to load from api.conf
        app_config = ApiConfigLoader.load()
        auth_service = app_config.services.get("auth")
        
        if auth_service:
            # Convert to old ServiceConfig format for compatibility
            headers: Dict[str, str] = {
                "Content-Type": "application/json",
                "User-Agent": app_config.user_agent,
            }
            headers.update(auth_service.headers)
            
            return OldServiceConfig(
                name=auth_service.name,
                base_url=auth_service.base_url,
                default_headers=headers,
                timeout=auth_service.timeout
            )
    except Exception:
        # If api.conf fails, continue to environment fallback
        pass
    
    # Fallback to environment variables (legacy mode)
    return OldServiceConfig(
        name="auth",
        base_url=os.getenv("AUTH_BASE_URL", "http://localhost:8085/auth"),
        default_headers={
            "Content-Type": "application/json",
            "User-Agent": os.getenv("E2E_USER_AGENT", "Auth-Test-Agent/1.0")
        },
        timeout=int(os.getenv("AUTH_TIMEOUT", "5000"))
    )


def get_auth_service_config() -> ServiceConfig:
    """
    Get the raw Auth service configuration from api.conf.
    
    Returns:
        ServiceConfig: Raw service configuration with all settings
        
    Raises:
        ValueError: If auth service not found in configuration
    """
    app_config = ApiConfigLoader.load()
    auth_service = app_config.services.get("auth")
    
    if not auth_service:
        raise ValueError("Auth service not found in api.conf configuration")
    
    return auth_service
