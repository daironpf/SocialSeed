"""Configuration loader for auth-service.

This module provides configuration loading specific to the auth-service service.
"""

from socialseed_e2e.core.config_loader import ApiConfigLoader, ServiceConfig


def get_auth_service_config() -> ServiceConfig:
    """Get configuration for auth-service service.

    Returns:
        ServiceConfig: Configuration object for the service

    Raises:
        ConfigError: If configuration cannot be loaded
    """
    loader = ApiConfigLoader()
    config = loader.load()
    return config.services["auth-service"]
