"""
ApiConfigLoader - Centralized Configuration Management

This module provides a centralized way to load and manage API configuration
from the api.conf file. It supports environment variable substitution and
provides easy access to all service configurations.
"""

import os
import re
from pathlib import Path
from typing import Dict, Any, Optional, List
from dataclasses import dataclass, field

# Try to import yaml, fallback to JSON if not available
import json

HAS_YAML = False
try:
    import yaml  # type: ignore
    HAS_YAML = True
except ImportError:
    pass  # YAML not available, will use JSON


@dataclass
class ServiceEndpoint:
    """Represents a single API endpoint configuration."""
    name: str
    path: str
    method: str = "POST"
    requires_auth: bool = False


@dataclass
class ServiceConfig:
    """Complete configuration for a microservice."""
    name: str
    base_url: str
    health_endpoint: str = "/actuator/health"
    port: int = 8080
    maven_module: str = ""
    timeout: int = 30000
    headers: Dict[str, str] = field(default_factory=dict)
    auto_start: bool = True
    required: bool = True
    endpoints: Dict[str, str] = field(default_factory=dict)


@dataclass
class ApiGatewayConfig:
    """API Gateway configuration."""
    enabled: bool = False
    url: str = ""
    prefix: str = ""
    auth_type: str = "none"
    auth_token: Optional[str] = None
    api_key_header: Optional[str] = None
    api_key_value: Optional[str] = None


@dataclass
class DatabaseConfig:
    """Database connection configuration."""
    host: str = "localhost"
    port: int = 5432
    database: str = ""
    username: str = ""
    password: str = ""
    enabled: bool = False


@dataclass
class TestDataConfig:
    """Test data generation configuration."""
    email_domain: str = "test.socialseed.com"
    password: str = "StrongPass123!"
    username_prefix: str = "testuser"
    step_delay: int = 100
    async_timeout: int = 10000
    max_retries: int = 3
    retry_backoff_ms: int = 1000


@dataclass
class SecurityConfig:
    """Security and SSL configuration."""
    verify_ssl: bool = True
    ssl_cert: Optional[str] = None
    ssl_key: Optional[str] = None
    ssl_ca: Optional[str] = None
    test_tokens: Dict[str, str] = field(default_factory=dict)


@dataclass
class ReportingConfig:
    """Test reporting configuration."""
    format: str = "console"
    save_logs: bool = True
    log_dir: str = "./logs"
    include_payloads: bool = False
    screenshot_on_failure: bool = False


@dataclass
class AppConfig:
    """Main application configuration container."""
    environment: str = "dev"
    timeout: int = 30000
    user_agent: str = "SocialSeed-E2E-Agent/2.0"
    verification_level: str = "strict"
    verbose: bool = True
    project_name: str = "SocialSeed"
    project_version: str = "0.0.0"
    api_gateway: ApiGatewayConfig = field(default_factory=ApiGatewayConfig)
    services: Dict[str, ServiceConfig] = field(default_factory=dict)
    databases: Dict[str, DatabaseConfig] = field(default_factory=dict)
    test_data: TestDataConfig = field(default_factory=TestDataConfig)
    security: SecurityConfig = field(default_factory=SecurityConfig)
    reporting: ReportingConfig = field(default_factory=ReportingConfig)


class ApiConfigLoader:
    """
    Loads and manages API configuration from api.conf file.
    
    Supports:
    - Environment variable substitution: ${VAR_NAME} or ${VAR_NAME:-default}
    - YAML or JSON format
    - Hot-reloading (reload config without restarting)
    - Singleton pattern for global access
    
    Usage:
        # Load default configuration
        config = ApiConfigLoader.load()
        
        # Get specific service configuration
        auth_config = config.services.get("auth")
        
        # Check if using API Gateway
        if config.api_gateway.enabled:
            base_url = config.api_gateway.url
    """
    
    _instance: Optional[AppConfig] = None
    _config_path: Optional[Path] = None
    
    @classmethod
    def load(cls, config_path: Optional[str] = None) -> AppConfig:
        """
        Load configuration from api.conf file.
        
        Args:
            config_path: Path to configuration file. If None, searches in:
                1. Environment variable E2E_CONFIG_PATH
                2. verify_services/api.conf (relative to project root)
                3. Current working directory ./api.conf
        
        Returns:
            AppConfig: Parsed configuration object
            
        Raises:
            FileNotFoundError: If configuration file not found
            ValueError: If configuration file is invalid
        """
        if cls._instance is not None and config_path is None:
            return cls._instance
        
        # Determine config file path
        if config_path is None:
            config_path = cls._find_config_file()
        
        config_file = Path(config_path)
        if not config_file.exists():
            raise FileNotFoundError(f"Configuration file not found: {config_path}")
        
        # Read and parse file
        content = config_file.read_text()
        
        # Substitute environment variables
        content = cls._substitute_env_vars(content)
        
        # Parse YAML or JSON
        if HAS_YAML:
            import yaml as yaml_module
            data = yaml_module.safe_load(content)
        else:
            import json as json_module
            data = json_module.loads(content)
        
        # Build configuration object
        config = cls._parse_config(data)
        cls._instance = config
        cls._config_path = config_file
        
        return config
    
    @classmethod
    def reload(cls) -> AppConfig:
        """Reload configuration from file (useful for hot-reloading)."""
        cls._instance = None
        if cls._config_path is not None:
            return cls.load(str(cls._config_path))
        return cls.load()
    
    @classmethod
    def get_config_path(cls) -> Optional[Path]:
        """Get the path of the currently loaded configuration file."""
        return cls._config_path
    
    @classmethod
    def _find_config_file(cls) -> str:
        """Find configuration file in common locations."""
        # Priority order:
        # 1. Environment variable
        if env_path := os.getenv("E2E_CONFIG_PATH"):
            return env_path
        
        # 2. Relative to project structure (verify_services/api.conf)
        # Try to find verify_services directory
        current_dir = Path.cwd()
        
        # Search up the directory tree for verify_services/api.conf
        for parent in [current_dir] + list(current_dir.parents):
            verify_services_dir = parent / "verify_services"
            if verify_services_dir.exists():
                config_file = verify_services_dir / "api.conf"
                if config_file.exists():
                    return str(config_file)
        
        # 3. Try e2e directory
        e2e_config = Path("verify_services/api.conf")
        if e2e_config.exists():
            return str(e2e_config)
        
        # 4. Current directory
        if Path("api.conf").exists():
            return "api.conf"
        
        raise FileNotFoundError(
            "Could not find api.conf. Please create it in verify_services/ "
            "or set E2E_CONFIG_PATH environment variable."
        )
    
    @classmethod
    def _substitute_env_vars(cls, content: str) -> str:
        """Substitute environment variables in configuration content."""
        # Pattern: ${VAR_NAME} or ${VAR_NAME:-default_value}
        pattern = r'\$\{(\w+)(?::-([^}]*))?\}'
        
        def replace_var(match):
            var_name = match.group(1)
            default_value = match.group(2)
            
            env_value = os.getenv(var_name)
            if env_value is not None:
                return env_value
            elif default_value is not None:
                return default_value
            else:
                # Keep original if no value found
                return match.group(0)
        
        return re.sub(pattern, replace_var, content)
    
    @classmethod
    def _parse_config(cls, data: Dict[str, Any]) -> AppConfig:
        """Parse raw dictionary into AppConfig object."""
        # General configuration
        general = data.get("general", {})
        project = general.get("project", {})
        
        config = AppConfig(
            environment=general.get("environment", "dev"),
            timeout=general.get("timeout", 30000),
            user_agent=general.get("user_agent", "SocialSeed-E2E-Agent/2.0"),
            verification_level=general.get("verification_level", "strict"),
            verbose=general.get("verbose", True),
            project_name=project.get("name", "SocialSeed"),
            project_version=project.get("version", "0.0.0"),
        )
        
        # API Gateway
        gateway_data = data.get("api_gateway", {})
        auth_data = gateway_data.get("auth", {})
        config.api_gateway = ApiGatewayConfig(
            enabled=gateway_data.get("enabled", False),
            url=gateway_data.get("url", ""),
            prefix=gateway_data.get("prefix", ""),
            auth_type=auth_data.get("type", "none"),
            auth_token=auth_data.get("bearer_token"),
            api_key_header=auth_data.get("api_key_header"),
            api_key_value=auth_data.get("api_key_value"),
        )
        
        # Services
        services_data = data.get("services", {})
        for service_name, service_data in services_data.items():
            config.services[service_name] = ServiceConfig(
                name=service_data.get("name", service_name),
                base_url=service_data.get("base_url", ""),
                health_endpoint=service_data.get("health_endpoint", "/actuator/health"),
                port=service_data.get("port", 8080),
                maven_module=service_data.get("maven_module", f"services/{service_name}"),
                timeout=service_data.get("timeout", config.timeout),
                headers=service_data.get("headers", {}),
                auto_start=service_data.get("auto_start", True),
                required=service_data.get("required", True),
                endpoints=service_data.get("endpoints", {}),
            )
        
        # Databases
        databases_data = data.get("databases", {})
        for db_name, db_data in databases_data.items():
            config.databases[db_name] = DatabaseConfig(
                host=db_data.get("host", "localhost"),
                port=db_data.get("port", 5432),
                database=db_data.get("database", ""),
                username=db_data.get("username", ""),
                password=db_data.get("password", ""),
                enabled=db_data.get("enabled", False),
            )
        
        # Test data
        test_data = data.get("test_data", {})
        user_data = test_data.get("user", {})
        timing_data = test_data.get("timing", {})
        retries_data = test_data.get("retries", {})
        config.test_data = TestDataConfig(
            email_domain=user_data.get("email_domain", "test.socialseed.com"),
            password=user_data.get("password", "StrongPass123!"),
            username_prefix=user_data.get("username_prefix", "testuser"),
            step_delay=timing_data.get("step_delay", 100),
            async_timeout=timing_data.get("async_timeout", 10000),
            max_retries=retries_data.get("max_attempts", 3),
            retry_backoff_ms=retries_data.get("backoff_ms", 1000),
        )
        
        # Security
        security_data = data.get("security", {})
        config.security = SecurityConfig(
            verify_ssl=security_data.get("verify_ssl", True),
            ssl_cert=security_data.get("ssl_cert"),
            ssl_key=security_data.get("ssl_key"),
            ssl_ca=security_data.get("ssl_ca"),
            test_tokens=security_data.get("test_tokens", {}),
        )
        
        # Reporting
        reporting_data = data.get("reporting", {})
        config.reporting = ReportingConfig(
            format=reporting_data.get("format", "console"),
            save_logs=reporting_data.get("save_logs", True),
            log_dir=reporting_data.get("log_dir", "./logs"),
            include_payloads=reporting_data.get("include_payloads", False),
            screenshot_on_failure=reporting_data.get("screenshot_on_failure", False),
        )
        
        return config
    
    @classmethod
    def get_service_url(cls, service_name: str, use_gateway: Optional[bool] = None) -> str:
        """
        Get the effective URL for a service.
        
        If API Gateway is enabled, returns gateway URL + service path.
        Otherwise, returns the service's base_url.
        
        Args:
            service_name: Name of the service
            use_gateway: Force gateway mode (None = use config setting)
        
        Returns:
            str: Full URL for the service
        """
        config = cls.load()
        service = config.services.get(service_name)
        
        if not service:
            raise ValueError(f"Service '{service_name}' not found in configuration")
        
        # Determine if we should use gateway
        should_use_gateway = use_gateway if use_gateway is not None else config.api_gateway.enabled
        
        if should_use_gateway and config.api_gateway.url:
            gateway_url = config.api_gateway.url.rstrip("/")
            prefix = config.api_gateway.prefix.rstrip("/")
            return f"{gateway_url}{prefix}/{service_name}"
        
        return service.base_url
    
    @classmethod
    def get_all_required_services(cls) -> List[str]:
        """Get list of services marked as required."""
        config = cls.load()
        return [
            name for name, service in config.services.items()
            if service.required
        ]
    
    @classmethod
    def get_auto_start_services(cls) -> List[str]:
        """Get list of services configured for auto-start."""
        config = cls.load()
        return [
            name for name, service in config.services.items()
            if service.auto_start
        ]
    
    @classmethod
    def get_service_by_maven_module(cls, maven_module: str) -> Optional[ServiceConfig]:
        """Find service configuration by Maven module name."""
        config = cls.load()
        for service in config.services.values():
            if service.maven_module == maven_module:
                return service
        return None


# Convenience functions for direct import
def get_config() -> AppConfig:
    """Get the global configuration instance."""
    return ApiConfigLoader.load()


def get_service_config(service_name: str) -> Optional[ServiceConfig]:
    """Get configuration for a specific service."""
    config = ApiConfigLoader.load()
    return config.services.get(service_name)


def get_service_url(service_name: str) -> str:
    """Get effective URL for a service (considers API Gateway)."""
    return ApiConfigLoader.get_service_url(service_name)
