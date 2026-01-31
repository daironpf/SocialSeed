"""
Configuration module - Unified configuration management.

This module provides configuration loading from api.conf file with fallback
to environment variables for backward compatibility.

Usage:
    # Modern approach using api.conf
    from .config_loader import ApiConfigLoader, get_config
    config = ApiConfigLoader.load()
    auth_config = config.services.get("auth")
    
    # Legacy approach (still supported)
    from .config import ConfigLoader
    auth_config = ConfigLoader.load_service_config("auth")
"""

import os
from pathlib import Path
from typing import Dict, Optional
from .models import ServiceConfig as OldServiceConfig, TestContext
from .config_loader import ApiConfigLoader, get_service_config as _get_service_config

class ConfigLoader:
    """
    Loads configuration from api.conf file with fallback to environment variables.
    
    This class maintains backward compatibility while migrating to the new
    api.conf-based configuration system.
    """
    
    @staticmethod
    def load_service_config(service_name: str) -> OldServiceConfig:
        """
        Loads configuration for a specific service.
        
        Priority order:
        1. Try to load from api.conf using ApiConfigLoader
        2. Fallback to environment variables
        3. Raise error if not found
        
        Args:
            service_name: Name of the service (e.g., "auth", "socialuser")
            
        Returns:
            ServiceConfig: Configuration for the service
            
        Raises:
            ValueError: If service configuration cannot be found
        """
        # First, try to load from api.conf
        try:
            new_config = _get_service_config(service_name)
            if new_config:
                # Convert new ServiceConfig to old format for compatibility
                return OldServiceConfig(
                    name=new_config.name,
                    base_url=new_config.base_url,
                    default_headers={
                        "Content-Type": "application/json",
                        "User-Agent": ApiConfigLoader.load().user_agent,
                        **new_config.headers  # Merge service-specific headers
                    },
                    timeout=new_config.timeout
                )
        except Exception:
            # If api.conf fails, continue to environment fallback
            pass
        
        # Fallback to environment variables (legacy mode)
        prefix = service_name.upper()
        base_url = os.getenv(f"{prefix}_BASE_URL")
        
        if not base_url:
            raise ValueError(
                f"Configuration for service '{service_name}' not found. "
                f"Please ensure it exists in api.conf or set {prefix}_BASE_URL environment variable."
            )
            
        return OldServiceConfig(
            name=service_name,
            base_url=base_url,
            default_headers={
                "Content-Type": "application/json",
                "User-Agent": os.getenv("E2E_USER_AGENT", "E2E-Agent/1.0"),
            },
            timeout=int(os.getenv(f"{prefix}_TIMEOUT", "30000"))
        )

    @staticmethod
    def load_test_context() -> TestContext:
        """Loads the full test context from api.conf or environment."""
        try:
            config = ApiConfigLoader.load()
            return TestContext(
                env=config.environment,
                metadata={
                    "project": config.project_name,
                    "version": config.project_version,
                    "api_gateway_enabled": config.api_gateway.enabled
                }
            )
        except Exception:
            # Fallback to environment variables
            return TestContext(
                env=os.getenv("E2E_ENV", "dev"),
                metadata={
                    "project": "SocialSeed",
                    "version": os.getenv("PROJECT_VERSION", "unknown")
                }
            )


# Re-export ApiConfigLoader for convenience
__all__ = ['ConfigLoader', 'ApiConfigLoader']
