

---

### 4.4 Change Password Flow

**Archivo**: `04_change_password_flow.py`

```python
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore


def run(auth_page: 'AuthPage') -> APIResponse:
    """
    Flujo de cambio de contraseña.
    
    Este módulo:
    1. Verifica que hay sesión activa
    2. Obtiene user_id si no está disponible
    3. Alterna entre dos passwords para permitir re-testing
    4. Ejecuta cambio de password
    5. Actualiza test_password en estado
    
    La alternancia de passwords permite ejecutar este módulo múltiples veces:
    - Primera vez: StrongPass1! → NewSecretPassword123!
    - Segunda vez: NewSecretPassword123! → StrongPass1!
    """
    print("Running change password flow...")
    
    # Precondición: sesión activa
    if not auth_page.is_logged_in:
        raise ValueError("Login must run first to set is_logged_in")

    # Obtener user_id si no lo tenemos (después de logout/re-login)
    if not auth_page.user_id:
        response = auth_page.get_user_by_email(auth_page.test_email or '')
        if response.status != 200 or not auth_page.user_id:
            raise ValueError("Could not get user_id. Ensure login has been performed.")
        print(f"  Retrieved user_id: {auth_page.user_id}")

    # Password actual (del estado)
    current_password: str = auth_page.test_password or 'StrongPass1!'
    
    # Alternar entre dos passwords
    new_password: str = 'NewSecretPassword123!' if current_password == 'StrongPass1!' else 'StrongPass1!'

    print(f"  Changing password from {current_password} to {new_password}...")
    
    # Ejecutar cambio
    response: APIResponse = auth_page.change_password(
        auth_page.user_id, 
        current_password, 
        new_password
    )

    if response.status == 200:
        print("✓ Password change successful")
        auth_page.test_password = new_password
    else:
        print(f"✗ Password change failed: {response.status}")
        raise AssertionError("Password change failed")

    return response
```

---

### 4.5 Change Username Flow

**Archivo**: `05_change_username_flow.py`

```python
import time
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore

def run(auth_page: 'AuthPage') -> APIResponse:
    """
    Flujo de cambio de username.
    
    Este módulo:
    1. Genera nuevo username único
    2. Cambia el username
    3. Verifica que el lookup por nuevo username funciona
    """
    print("Running change username flow...")
    
    # Precondición: sesión activa
    if not auth_page.is_logged_in or not auth_page.auth_result:
        raise ValueError("Login must run first")

    # Generar nuevo username único
    new_username: str = f"cool_user_{int(time.time())}"
    print(f"  Changing username to {new_username}...")
    
    # Cambiar username
    response: APIResponse = auth_page.change_username(new_username)

    if response.status == 200:
        print("✓ Username change successful")
        auth_page.test_username = new_username
    else:
        print(f"✗ Change username failed: {response.status}")
        raise AssertionError("Change username failed")

    # Verificación: lookup por nuevo username debe funcionar
    print(f"  Verifying lookup by new username {new_username}...")
    response = auth_page.get_user_by_username(new_username)
    
    if response.status == 200:
        data = response.json()
        user_data = data.get('data', data)
        if user_data.get('username') == new_username:
            print("✓ Lookup by new username successful")
        else:
            raise AssertionError("Username mismatch")
    else:
        raise AssertionError("Lookup by new username failed")

    return response
```

---

### 4.6 Refresh Flow

**Archivo**: `06_refresh_flow.py`

```python
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore

def run(auth_page: 'AuthPage') -> APIResponse:
    """
    Flujo de renovación de token.
    
    Este módulo:
    1. Verifica que hay sesión activa con refresh token
    2. Renueva el token de acceso
    3. Verifica que se recibieron nuevos tokens
    
    El refresh token es útil cuando el access token expira (ej: después de 15 min).
    """
    print("Running token refresh flow...")
    
    # Precondición: sesión activa
    if not auth_page.is_logged_in or not auth_page.auth_result:
        raise ValueError("Login must run first")

    # Obtener refresh token
    refresh_token = auth_page.auth_result.refreshToken
    if not refresh_token:
        raise ValueError("No refresh token available")

    # Renovar token
    response: APIResponse = auth_page.refresh_token()

    if response.status == 200:
        print("✓ Token refresh successful")
        # auth_page.auth_result ya fue actualizado en refresh_token()
    else:
        print(f"✗ Token refresh failed: {response.status}")
        raise AssertionError("Token refresh failed")

    return response
```

---

### 4.7 Logout Flow

**Archivo**: `99_logout_flow.py`

```python
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING, TYPE_CHECKING as TC_IMPORT
from verify_services.e2e.services.auth.auth_page import AuthPage  # type: ignore

def run(auth_page: 'AuthPage') -> APIResponse:
    """
    Flujo de cierre de sesión.
    
    Este módulo:
    1. Verifica que hay sesión activa
    2. Cierra sesión
    3. Verifica éxito (200 o 204)
    
    El estado se limpia automáticamente en auth_page.logout()
    """
    print("Running logout flow...")
    
    # Precondición: sesión activa
    if not auth_page.is_logged_in or not auth_page.auth_result:
        raise ValueError("Login must run first")

    print("  Logging out...")
    response: APIResponse = auth_page.logout()

    # 200 o 204 son ambos válidos para logout
    if response.status in [200, 204]:
        print("✓ Logout successful")
        # Estado ya fue limpiado por auth_page.logout()
    else:
        print(f"✗ Logout failed: {response.status}")
        raise AssertionError("Logout failed")

    return response
```

---

## 5. Runners (Ejecutores)

### 5.1 run_auth_tests.py

**Archivo**: `verify_services/e2e/run_auth_tests.py`

**Propósito**: Ejecutor simple usando el orquestador para tests de auth.

```python
#!/usr/bin/env python3
"""
Simple runner for auth E2E tests using the modular framework.

Este script es el más simple: usa TestOrchestrator para descubrir
y ejecutar automáticamente todos los módulos de auth.
"""
from verify_services.e2e.core.test_orchestrator import TestOrchestrator
from verify_services.e2e.services.auth.auth_page import AuthPage

def main():
    """
    Función principal del runner.
    
    Flujo:
    1. Crea orquestador
    2. Descubre módulos automáticamente
    3. Ejecuta todos los tests
    4. Reporta resultado
    """
    # Crear orquestador con paths por defecto
    orchestrator = TestOrchestrator()
    
    # Descubrir todos los módulos en services/*/modules/
    orchestrator.discover_modules()
    
    # Factory function: crea nuevo AuthPage para cada servicio
    def context_factory():
        page = AuthPage()
        page.setup()
        return page

    try:
        # Ejecutar todos los tests
        orchestrator.run_all_tests(context_factory)
        print("All tests passed!")
    except Exception as e:
        print(f"Tests failed: {e}")
        exit(1)

if __name__ == "__main__":
    main()
```

**Uso:**
```bash
python verify_services/e2e/run_auth_tests.py
```

---

### 5.2 run_complete_verification.py

**Archivo**: `verify_services/run_complete_verification.py`

**Propósito**: Ejecutor completo con gestión de servicios y orden de ejecución específico.

```python
#!/usr/bin/env python3
"""
Complete E2E Verification Runner

Este es el ejecutor más completo. Características:
- Verifica si servicios están corriendo
- Los inicia automáticamente si es necesario (Maven + dev profile)
- Ejecuta tests en orden específico (no alfabético)
- Usa configuración desde api.conf
- Limpia servicios al final
"""
import sys
import os
import subprocess
import time
from pathlib import Path
sys.path.insert(0, os.path.dirname(os.path.dirname(__file__)))

import requests
from typing import Optional, Dict, Any, List, Tuple, Callable

from verify_services.e2e.services.auth.auth_page import AuthPage
from verify_services.e2e.core.config_loader import ApiConfigLoader
from verify_services.e2e.core.loaders import ModuleLoader

# Type alias para función de módulo
ModuleFunc = Callable[[AuthPage], Any]

# Carga dinámica de módulos
_module_loader = ModuleLoader()
_modules_path = Path(__file__).parent / "e2e" / "services" / "auth" / "modules"
_loaded_modules = _module_loader.discover_runnables(_modules_path)

# Mapeo de nombres a funciones
_module_map = {mod.__module__.split('.')[-1]: mod for mod in _loaded_modules}

# Exportar módulos comunes
register_flow = _module_map.get('01_register_flow')
login_flow = _module_map.get('02_login_flow')
# ... (todos los demás módulos)


class ServiceManager:
    """
    Gestiona inicio y health checks de servicios usando api.conf.
    
    Esta clase:
    - Lee configuración de servicios desde api.conf
    - Verifica si un servicio está corriendo (health check)
    - Lo inicia automáticamente con Maven si es necesario
    - Limpia servicios al finalizar
    """
    
    def __init__(self) -> None:
        self.processes: Dict[str, subprocess.Popen] = {}
        # Cargar configuración desde api.conf
        from verify_services.e2e.core.config_loader import ApiConfigLoader
        self.config = ApiConfigLoader.load()

    def check_service_health(self, url: str) -> bool:
        """
        Verifica si un servicio está saludable.
        
        Args:
            url: URL del endpoint de health (ej: http://localhost:8085/actuator/health)
            
        Returns:
            bool: True si responde HTTP 200
        """
        try:
            verify_ssl = self.config.security.verify_ssl
            response = requests.get(url, timeout=5, verify=verify_ssl)
            return response.status_code == 200
        except Exception:
            return False

    def wait_for_service(self, url: str, service_name: str, timeout: int = 120) -> bool:
        """
        Espera a que un servicio esté disponible.
        
        Args:
            url: URL del health endpoint
            service_name: Nombre para mensajes
            timeout: Tiempo máximo de espera en segundos
            
        Returns:
            bool: True si el servicio está up, False si timeout
        """
        print(f"Waiting for {service_name} to be up...")
        start_time = time.time()
        
        while time.time() - start_time < timeout:
            if self.check_service_health(url):
                print(f"✓ {service_name} is UP!")
                return True
            time.sleep(2)
            
        print(f"✗ {service_name} failed to start within {timeout} seconds.")
        return False

    def start_service(self, service_name: str) -> bool:
        """
        Inicia un servicio usando configuración de api.conf.
        
        Args:
            service_name: Nombre del servicio (ej: "auth", "socialuser")
            
        Returns:
            bool: True si está corriendo o se inició correctamente
        """
        service_config = self.config.services.get(service_name)
        if not service_config:
            print(f"⚠ Service '{service_name}' not found in api.conf")
            return False

        # Construir URL de health check
        base_url = service_config.base_url.rstrip('/')
        health_url = f"{base_url}{service_config.health_endpoint}"

        # Verificar si ya está corriendo
        if self.check_service_health(health_url):
            print(f"✓ {service_config.name} is already running")
            return True

        # Verificar si auto_start está habilitado
        if not service_config.auto_start:
            if service_config.required:
                print(f"✗ {service_config.name} is required but not running")
                return False
            else:
                print(f"⚠ {service_config.name} not running but not required")
                return True

        # Iniciar servicio con Maven
        print(f"Starting {service_config.name} with dev profile...")
        try:
            cmd = [
                "mvn", "spring-boot:run",
                "-pl", service_config.maven_module,
                "-Dspring-boot.run.profiles=dev",
                f"-Dspring-boot.run.jvmArguments=-Dserver.port={service_config.port}"
            ]
            
            # Determinar directorio raíz del proyecto
            config_path = ApiConfigLoader.get_config_path()
            project_root = config_path.parent.parent if config_path else Path.cwd()
            
            # Ejecutar Maven en background
            process = subprocess.Popen(
                cmd,
                stdout=subprocess.DEVNULL,
                stderr=subprocess.DEVNULL,
                cwd=str(project_root)
            )
            self.processes[service_name] = process
            
            # Esperar a que esté listo
            return self.wait_for_service(health_url, service_config.name)
            
        except Exception as e:
            print(f"✗ Failed to start {service_config.name}: {e}")
            return not service_config.required  # Fallar solo si es requerido

    def start_auth_service(self) -> bool:
        """Inicia auth-service."""
        return self.start_service("auth")

    def start_socialuser_service(self) -> bool:
        """Inicia socialuser-service."""
        return self.start_service("socialuser")

    def cleanup(self) -> None:
        """Detiene todos los servicios iniciados."""
        print("\nCleaning up services...")
        for name, process in self.processes.items():
            try:
                print(f"Stopping {name} service...")
                process.terminate()
                process.wait(timeout=10)
                print(f"✓ {name} service stopped")
            except Exception as e:
                print(f"⚠ Could not stop {name} service: {e}")


def create_auth_context() -> AuthPage:
    """Factory: Crea y configura AuthPage."""
    page = AuthPage()
    page.setup()
    return page


def main() -> int:
    """
    Función principal del runner completo.
    
    Flujo de ejecución:
    1. Iniciar servicios necesarios
    2. Crear contexto de auth
    3. Ejecutar tests en orden específico
    4. Limpiar recursos
    5. Retornar código de salida
    """
    print("=== Starting E2E Verification for SocialSeed ===")

    service_manager = ServiceManager()

    # Iniciar servicios requeridos
    if not service_manager.start_auth_service():
        print("✗ Cannot proceed without auth-service")
        return 1

    # Iniciar servicios opcionales
    socialuser_started = service_manager.start_socialuser_service()

    # Crear contexto
    auth_page = create_auth_context()
    success = True

    try:
        print("\n=== Running Auth Service E2E Tests ===")

        # Orden específico de ejecución (no alfabético)
        modules: List[Tuple[str, Optional[ModuleFunc]]] = [
            ("Registration Flow", register_flow),
            ("Login Flow", login_flow),
            ("Refresh Token Flow", refresh_flow),
            ("Logout Flow", logout_flow),
            ("Re-Login", login_flow),
            ("Change Password Flow", change_password_flow),
            ("Verify New Password", login_flow),
            ("Forgot Password Flow", forgot_password_flow),
            ("Reset Password Flow (Negative)", reset_password_flow),
            ("Verify Email Flow", verify_email_flow),
            ("Re-Login for Username Change", login_flow),
            ("Change Username Flow", change_username_flow),
            ("Re-Login for Email Change", login_flow),
            ("Change Email Flow", change_email_flow),
            ("Resend Verification Flow", resend_verification_flow),
            ("Credential Expiration Flow", credential_expiration_flow)
        ]

        # Ejecutar cada módulo
        for module_name, module_func in modules:
            print(f"\n--- {module_name} ---")
            
            if module_func is None:
                print(f"⚠ {module_name} module not found, skipping")
                continue
                
            try:
                module_func(auth_page)
                print(f"✓ {module_name} passed")
            except Exception as e:
                print(f"✗ {module_name} failed: {e}")
                import traceback
                traceback.print_exc()
                success = False
                break

        # Reportar resultado final
        if success:
            print("\n🎉 All Auth E2E tests passed!")
        else:
            print("\n❌ Some Auth E2E tests failed!")

    finally:
        # Limpieza garantizada
        auth_page.teardown()
        service_manager.cleanup()

    return 0 if success else 1


if __name__ == "__main__":
    exit(main())
```

**Uso:**
```bash
python verify_services/run_complete_verification.py
```

---

## 6. Guía de Uso Paso a Paso

### 6.1 Configuración Inicial

1. **Crear api.conf:**
```bash
cp verify_services/api.conf.example verify_services/api.conf
```

2. **Editar configuración:**
```yaml
services:
  auth:
    base_url: http://localhost:8085/auth
    port: 8085
    maven_module: services/auth-service
    auto_start: true
    required: true
```

3. **Verificar dependencias:**
```bash
pip install playwright pydantic pyyaml requests
playwright install
```

### 6.2 Ejecutar Tests

**Opción A - Runner completo (recomendado):**
```bash
python verify_services/run_complete_verification.py
```

**Opción B - Runner simple:**
```bash
python verify_services/e2e/run_auth_tests.py
```

**Opción C - Ejecución manual:**
```python
from verify_services.e2e.services.auth.auth_page import AuthPage
from verify_services.e2e.services.auth.modules import register_flow, login_flow

auth = AuthPage()
auth.setup()

try:
    register_flow.run(auth)
    login_flow.run(auth)
finally:
    auth.teardown()
```

### 6.3 Crear Nuevo Módulo de Test

1. **Crear archivo:**
```bash
touch verify_services/e2e/services/auth/modules/99_my_test.py
```

2. **Implementar módulo:**
```python
from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from verify_services.e2e.services.auth.auth_page import AuthPage

def run(auth_page: 'AuthPage') -> APIResponse:
    """Mi test personalizado."""
    print("Running my test...")
    
    # Precondición: sesión activa
    if not auth_page.is_logged_in:
        raise ValueError("Must be logged in")
    
    # Mi lógica de test
    response = auth_page.get("/mi-endpoint")
    
    if response.status == 200:
        print("✓ My test passed")
    else:
        raise AssertionError("My test failed")
    
    return response
```

3. **Ejecutar:**
```bash
python verify_services/run_complete_verification.py
```

### 6.4 Depuración

**Activar modo verbose en api.conf:**
```yaml
general:
  verbose: true
```

**Ver logs de un módulo específico:**
```python
# En tu módulo
response = auth_page.post("/endpoint", data)
print(f"Status: {response.status}")
print(f"Body: {response.body()}")
print(f"Headers: {response.headers}")
```

**Verificar configuración cargada:**
```python
from verify_services.e2e.core.config_loader import ApiConfigLoader

config = ApiConfigLoader.load()
print(f"Auth URL: {config.services['auth'].base_url}")
print(f"Timeout: {config.timeout}")
```

---

## Resumen de Archivos

| Archivo | Propósito | Tipo |
|---------|-----------|------|
| `api.conf` | Configuración centralizada | YAML |
| `core/base_page.py` | HTTP methods genéricos | Core |
| `core/config_loader.py` | Carga de api.conf | Core |
| `core/config.py` | Wrapper de compatibilidad | Core |
| `core/loaders.py` | Carga dinámica de módulos | Core |
| `core/test_orchestrator.py` | Orquestador de tests | Core |
| `core/interfaces.py` | Protocolos de tipado | Core |
| `core/models.py` | Modelos Pydantic | Core |
| `core/headers.py` | Headers por defecto | Core |
| `core/check_deps.py` | Validador de arquitectura | Core |
| `services/auth/auth_page.py` | Página de auth | Service |
| `services/auth/config.py` | Config de auth | Service |
| `services/auth/data_schema.py` | Modelos de auth | Service |
| `modules/*_flow.py` | Tests específicos | Test Module |
| `run_auth_tests.py` | Runner simple | Runner |
| `run_complete_verification.py` | Runner completo | Runner |

---

**Fin de la documentación completa del código.**
