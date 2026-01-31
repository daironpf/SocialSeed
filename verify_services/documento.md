# SocialSeed E2E Testing Framework - Guía Completa de Uso

> **Versión**: 2.0 - Core Desacoplado y Agnóstico  
> **Framework**: Playwright (API Testing) + Python 3.12  
> **Arquitectura**: Hexagonal / Service-Agnostic

---

## 📚 Tabla de Contenidos

1. [Visión General](#visión-general)
2. [Arquitectura](#arquitectura)
3. [Instalación y Configuración](#instalación-y-configuración)
4. [Guía Rápida de Uso](#guía-rápida-de-uso)
5. [Creando Nuevos Servicios](#creando-nuevos-servicios)
6. [Creando Módulos de Prueba](#creando-módulos-de-prueba)
7. [Páginas de Servicio (Service Pages)](#páginas-de-servicio)
8. [Orquestación de Tests](#orquestación-de-tests)
9. [Patrones Avanzados](#patrones-avanzados)
10. [AI Governance y Reglas de Arquitectura](#ai-governance)
11. [Troubleshooting](#troubleshooting)
12. [Referencia de API](#referencia-de-api)

---

## Visión General

El Framework E2E de SocialSeed es un motor de testing agnóstico de servicios diseñado para:

- **Desacoplamiento Total**: El core (`e2e/core/`) es 100% independiente de cualquier servicio específico
- **Configuración vía Entorno**: Sin URLs ni strings hardcodeados
- **Descubrimiento Dinámico**: Auto-descubre módulos de prueba en tiempo de ejecución
- **Type Safety**: Usa Python type hints y Pydantic para validación
- **Extensibilidad**: Fácil de extender a nuevos microservicios

### Características Clave

| Feature | Descripción |
|---------|-------------|
| **Zero-Coupling** | El core no conoce "SocialSeed", "Auth", o cualquier servicio |
| **Dynamic Loading** | Carga automática de módulos con `importlib` |
| **Protocol-Based** | Usa `typing.Protocol` para interfaces estructurales |
| **Centralized Config** | Archivo `api.conf` único para toda la configuración |
| **API Gateway Support** | Soporte para API Gateway o conexiones directas |
| **Environment Override** | Variables de entorno pueden sobreescribir api.conf |
| **Service Manager** | Inicia servicios automáticamente si no están corriendo |
| **State Management** | Páginas mantienen estado entre llamadas (tokens, usuarios, etc.) |

---

## Arquitectura

```
verify_services/
├── e2e/
│   ├── core/                    # Motor agnóstico (100% desacoplado)
│   │   ├── base_page.py         # BasePage: HTTP methods con Playwright
│   │   ├── test_orchestrator.py # Descubrimiento y ejecución dinámica
│   │   ├── loaders.py           # Carga dinámica de módulos
│   │   ├── interfaces.py        # Protocols (IServicePage, ITestModule)
│   │   ├── config_loader.py     # ApiConfigLoader desde api.conf
│   │   ├── models.py            # Pydantic models (ServiceConfig, TestContext)
│   │   ├── headers.py           # Constantes de headers HTTP
│   │   └── check_deps.py        # Validador de zero-coupling
│   │
│   └── services/                # Implementaciones específicas por servicio
│       └── auth/
│           ├── auth_page.py     # AuthPage extiende BasePage
│           ├── config.py        # Config específica de Auth
│           ├── data_schema.py   # DTOs Pydantic (UserDTO, AuthResult, etc.)
│           └── modules/         # Módulos de prueba
│               ├── 01_register_flow.py
│               ├── 02_login_flow.py
│               └── ...
│
└── .agent/                      # (Opcional) Governance Layer
    ├── rules.yaml               # Reglas arquitectónicas para IA
    ├── workflow.md              # Protocolo operativo para agentes
    └── templates/               # Plantillas para generación automática
        ├── test_module_template.py
        └── service_page_template.py
```

### Flujo de Datos

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│   Test Module   │────▶│   Service Page   │────▶│    BasePage     │
│   (run func)    │     │  (AuthPage, etc) │     │  (HTTP Methods) │
└─────────────────┘     └──────────────────┘     └─────────────────┘
        │                        │                        │
        │                        │                        ▼
        │                        │               ┌─────────────────┐
        │                        │               │   Playwright    │
        │                        │               │  API Context    │
        │                        │               └─────────────────┘
        │                        │
        ▼                        ▼
┌─────────────────────────────────────────────────────────────────┐
│                    TestOrchestrator                               │
│          (Descubrimiento dinámico + Ejecución)                   │
└─────────────────────────────────────────────────────────────────┘
```

---

## Instalación y Configuración

### 1. Prerrequisitos

```bash
# Python 3.12+
python --version

# Dependencias
pip install playwright pydantic pyyaml requests

# Instalar browsers de Playwright
playwright install
```

### 2. Configuración Centralizada (api.conf)

El framework usa un archivo de configuración centralizado `api.conf` (formato YAML) ubicado en `verify_services/`:

#### Estructura de api.conf

```yaml
# === Configuración General ===
general:
  environment: dev
  timeout: 30000
  user_agent: "SocialSeed-E2E-Agent/2.0"
  verification_level: strict
  verbose: true

# === API Gateway (Opcional) ===
api_gateway:
  enabled: false
  url: "http://localhost:8080"
  prefix: "/api/v1"
  auth:
    type: "none"  # none | bearer | api_key

# === Servicios ===
services:
  auth:
    name: "auth-service"
    base_url: "http://localhost:8085/auth"
    health_endpoint: "/actuator/health"
    port: 8085
    maven_module: "services/auth-service"
    timeout: 5000
    auto_start: true
    required: true

  socialuser:
    name: "socialuser-service"
    base_url: "http://localhost:8090"
    health_endpoint: "/actuator/health"
    port: 8090
    maven_module: "services/socialuser-service"
    timeout: 5000
    auto_start: false
    required: false
```

#### Variables de Entorno (Opcional)

Puedes usar variables de entorno en `api.conf` con sintaxis `${VAR}` o `${VAR:-default}`:

```yaml
services:
  auth:
    base_url: ${AUTH_BASE_URL:-http://localhost:8085/auth}
```

También puedes configurar la ubicación del archivo:

```bash
export E2E_CONFIG_PATH=/path/to/custom/api.conf
```
# E2E_VERIFY_SSL=false
```

### 3. Verificación de Dependencias

Ejecuta el validador de zero-coupling:

```bash
python -m verify_services.e2e.core.check_deps
```

**Éxito**: Sin output (o "✓ Core integrity check passed")  
**Fallo**: Lista de imports prohibidos encontrados

---

## Guía Rápida de Uso

### Ejecutar Todos los Tests (Recomendado)

```bash
# Desde la raíz del proyecto
python verify_services/run_complete_verification.py
```

Este script:
1. Verifica si los servicios están corriendo
2. Los inicia automáticamente si es necesario (perfil `dev`)
3. Ejecuta todos los flujos de Auth en orden lógico
4. Hace cleanup al final

### Ejecutar Tests Dinámicos (Con Orquestador)

```bash
python verify_services/e2e/run_auth_tests.py
```

Este runner:
- Usa `TestOrchestrator` para auto-descubrir módulos
- Ejecuta tests en orden alfabético (01_, 02_, etc.)
- No inicia servicios automáticamente

### Ejecutar un Módulo Específico

```python
from verify_services.e2e.services.auth.auth_page import AuthPage
from verify_services.e2e.services.auth.modules import register_flow

page = AuthPage()
page.setup()

try:
    register_flow.run(page)
finally:
    page.teardown()
```

---

## Creando Nuevos Servicios

### Paso 1: Crear Estructura de Directorios

```bash
mkdir -p verify_services/e2e/services/{nombre-servicio}/modules
touch verify_services/e2e/services/{nombre-servicio}/__init__.py
```

### Paso 2: Crear `data_schema.py`

Define los DTOs de tu servicio usando Pydantic:

```python
from pydantic import BaseModel
from typing import Optional, List

class CreateEntityRequest(BaseModel):
    name: str
    description: Optional[str] = None

class EntityResponse(BaseModel):
    id: str
    name: str
    created_at: str

# Endpoints
CREATE_ENDPOINT = "/entities"
GET_ENDPOINT = "/entities/{id}"
```

### Paso 3: Actualizar `api.conf`

Agrega tu servicio al archivo `api.conf`:

```yaml
services:
  myservice:
    name: "myservice"
    base_url: ${MYSERVICE_BASE_URL:-http://localhost:8086}
    health_endpoint: "/actuator/health"
    port: 8086
    maven_module: "services/myservice"
    timeout: 5000
    auto_start: true
    required: true
    endpoints:
      create: "/entities"
      get: "/entities/{id}"
```

### Paso 4: Crear `config.py` (Opcional)

Si necesitas lógica adicional, crea un config.py:

```python
from ...core.config_loader import ApiConfigLoader, get_service_config
from ...core.models import ServiceConfig

def get_myservice_config() -> ServiceConfig:
    """Configuración para el nuevo servicio desde api.conf."""
    return get_service_config("myservice")
```

### Paso 4: Crear Service Page

```python
from verify_services.e2e.core.base_page import BasePage
from .data_schema import EntityResponse, CreateEntityRequest
from .config import get_service_config
from playwright.sync_api import APIResponse
from typing import Optional

class MyServicePage(BasePage):
    """Hub para MyService: maneja estado y orquesta módulos."""

    def __init__(self, playwright=None, base_url=None):
        config = get_service_config()
        url = base_url or config.base_url
        super().__init__(url, playwright, default_headers=config.default_headers)
        
        # Estado compartido entre módulos
        self.current_entity: Optional[EntityResponse] = None
        self.auth_token: Optional[str] = None

    def create_entity(self, name: str, description: str = None) -> APIResponse:
        """Crear entidad y actualizar estado."""
        request = CreateEntityRequest(name=name, description=description)
        response = self.post("/entities", data=request.model_dump())
        
        if response.ok:
            data = response.json().get('data', {})
            self.current_entity = EntityResponse(**data)
        
        return response

    def get_entity(self, entity_id: str) -> APIResponse:
        """Obtener entidad por ID."""
        return self.get(f"/entities/{entity_id}")

    def set_auth_token(self, token: str):
        """Setter para token de autenticación."""
        self.auth_token = token
        # Actualizar headers para próximas requests
        self.default_headers["Authorization"] = f"Bearer {token}"
```

### Paso 5: Agregar al ServiceManager

Edita `run_complete_verification.py` y agrega tu servicio:

```python
def start_myservice(self) -> bool:
    """Start myservice with dev profile if not running."""
    url = "http://localhost:8086/actuator/health"
    
    if self.check_service_health(url):
        print("✓ myservice is already running")
        return True
    
    print("Starting myservice with dev profile...")
    try:
        cmd = [
            "mvn", "spring-boot:run",
            "-pl", "services/myservice",
            "-Dspring-boot.run.profiles=dev",
            "-Dspring-boot.run.jvmArguments=-Dserver.port=8086"
        ]
        process = subprocess.Popen(
            cmd,
            stdout=subprocess.DEVNULL,
            stderr=subprocess.DEVNULL,
            cwd="/home/dairon/proyectos/SocialSeed"
        )
        self.processes["myservice"] = process
        return self.wait_for_service(url, "myservice")
    except Exception as e:
        print(f"✗ Failed to start myservice: {e}")
        return False
```

### Paso 6: Actualizar Variables de Entorno

```env
MYSERVICE_BASE_URL=http://localhost:8086
MYSERVICE_TIMEOUT=5000
```

---

## Creando Módulos de Prueba

### Convenciones de Nomenclatura

- **Prefijo numérico**: `01_`, `02_`, etc. para control de orden
- **Sufijo descriptivo**: `_flow.py`, `_test.py`, `_scenario.py`
- **Nombre de función**: Siempre debe existir `run(context)`

### Estructura Básica de un Módulo

```python
# myservice/modules/01_create_entity_flow.py

from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from verify_services.e2e.services.myservice.myservice_page import MyServicePage

def run(service_page: 'MyServicePage') -> APIResponse:
    """
    Flujo de creación de entidad.
    
    Args:
        service_page: Instancia de MyServicePage con estado compartido
        
    Returns:
        APIResponse: Respuesta HTTP de la última operación
        
    Raises:
        AssertionError: Si el flujo falla
    """
    print("Running create entity flow...")
    
    # Preparar datos de prueba
    entity_name = f"TestEntity_{int(time.time())}"
    
    # Ejecutar acción
    response = service_page.create_entity(name=entity_name, description="Test")
    
    # Validar resultado
    if response.status in [200, 201]:
        print(f"✓ Entity created: {service_page.current_entity.id}")
    else:
        print(f"✗ Failed: {response.status}")
        raise AssertionError("Entity creation failed")
    
    return response
```

### Patrones Avanzados de Módulos

#### 1. Dependencia de Estado (Chaining)

```python
# 02_update_entity_flow.py

def run(service_page: 'MyServicePage') -> APIResponse:
    """Actualizar entidad creada previamente."""
    # Requiere que 01_create_entity_flow haya corrido antes
    if not service_page.current_entity:
        raise ValueError("No entity available. Run create flow first.")
    
    entity_id = service_page.current_entity.id
    response = service_page.update_entity(
        entity_id=entity_id,
        new_name="UpdatedName"
    )
    
    assert response.status == 200, "Update failed"
    return response
```

#### 2. Módulos de Validación Negativa

```python
# 03_invalid_input_flow.py

def run(service_page: 'MyServicePage') -> APIResponse:
    """Probar manejo de input inválido."""
    # Enviar datos inválidos
    response = service_page.create_entity(name="")  # Empty name
    
    # Esperar error 400
    assert response.status == 400, f"Expected 400, got {response.status}"
    print("✓ Correctly rejected invalid input")
    return response
```

#### 3. Módulos Multi-Paso

```python
# 04_full_workflow_flow.py

def run(service_page: 'MyServicePage') -> APIResponse:
    """Flujo completo: crear → leer → actualizar → eliminar."""
    # Create
    response = service_page.create_entity(name="WorkflowTest")
    assert response.ok
    entity_id = service_page.current_entity.id
    
    # Read
    response = service_page.get_entity(entity_id)
    assert response.status == 200
    
    # Update
    response = service_page.update_entity(entity_id, new_name="Updated")
    assert response.ok
    
    # Delete
    response = service_page.delete_entity(entity_id)
    assert response.status in [200, 204]
    
    print("✓ Full workflow completed")
    return response
```

---

## Páginas de Servicio

### Responsabilidades de una Service Page

1. **State Management**: Mantener tokens, IDs, datos de usuario, etc.
2. **API Wrappers**: Métodos que llaman a endpoints específicos
3. **Response Parsing**: Extraer y almacenar datos relevantes
4. **Error Handling**: Convertir errores HTTP en excepciones claras

### Ejemplo Completo: AuthPage

```python
class AuthPage(BasePage):
    """
    Hub para auth service.
    
    Attributes:
        current_user: Usuario actualmente logueado
        auth_result: Tokens y roles del usuario
        is_logged_in: Estado de sesión
        test_*: Datos de prueba compartidos entre módulos
    """

    def __init__(self, playwright=None, base_url=None):
        config = get_auth_config()
        super().__init__(base_url or config.base_url, playwright, config.default_headers)
        
        # State
        self.current_user: Optional[UserDTO] = None
        self.auth_result: Optional[AuthResult] = None
        self.is_logged_in: bool = False
        
        # Test Data (compartido entre módulos)
        self.test_email: Optional[str] = None
        self.test_username: Optional[str] = None
        self.test_password: Optional[str] = None
        self.user_id: Optional[str] = None

    def login(self, email: str, password: str) -> APIResponse:
        """
        Login y actualización automática de estado.
        
        Si es exitoso:
        - Actualiza auth_result con tokens
        - Marca is_logged_in = True
        - Establece current_user
        """
        response = self.post("/login", {"email": email, "password": password})
        
        if response.ok:
            data = response.json().get('data', {})
            self.auth_result = AuthResult(**data)
            self.is_logged_in = True
            self.current_user = UserDTO(
                id="temp", 
                username="temp", 
                email=email, 
                roles=self.auth_result.roles or []
            )
        
        return response

    def logout(self) -> APIResponse:
        """Logout y limpieza de estado."""
        headers = {"Authorization": f"Bearer {self.auth_result.token}"} if self.auth_result else {}
        response = self.post("/logout", data={"refreshToken": self.auth_result.refreshToken}, headers=headers)
        
        if response.ok:
            self.is_logged_in = False
            self.current_user = None
            self.auth_result = None
        
        return response
```

### Métodos HTTP Heredados de BasePage

```python
# Todos disponibles en cualquier ServicePage

response = page.get("/endpoint", params={"key": "value"})
response = page.post("/endpoint", data={"key": "value"})
response = page.put("/endpoint", data={"key": "value"})
response = page.patch("/endpoint", data={"key": "value"})
response = page.delete("/endpoint")

# Headers personalizados por request
response = page.post("/endpoint", data={}, headers={"X-Custom": "value"})

# Obtener texto de respuesta
text = page.get_response_text(response)
```

---

## Orquestación de Tests

### Opción 1: Ejecución Manual Controlada

Usar cuando necesitas control exacto del orden y manejo de estado:

```python
from verify_services.e2e.services.auth.auth_page import AuthPage

# Setup
page = AuthPage()
page.setup()

try:
    # Flujos en orden específico
    register_flow.run(page)
    login_flow.run(page)
    change_password_flow.run(page)
    # Re-login para verificar nuevo password
    login_flow.run(page)
finally:
    # Cleanup garantizado
    page.teardown()
```

### Opción 2: TestOrchestrator (Descubrimiento Automático)

Usar cuando los módulos son independientes y pueden ejecutarse secuencialmente:

```python
from verify_services.e2e.core.test_orchestrator import TestOrchestrator
from verify_services.e2e.services.auth.auth_page import AuthPage

# Inicializar
orchestrator = TestOrchestrator()
orchestrator.discover_modules()

# Context factory: crea nuevo contexto por servicio
def context_factory():
    page = AuthPage()
    page.setup()
    return page

# Ejecutar todos
orchestrator.run_all_tests(context_factory)
```

### Opción 3: Integración con Pytest

```python
# conftest.py
import pytest
from verify_services.e2e.core.test_orchestrator import TestOrchestrator

@pytest.fixture(scope="session")
def orchestrator():
    orch = TestOrchestrator()
    orch.discover_modules()
    return orch

@pytest.fixture
def auth_page():
    page = AuthPage()
    page.setup()
    yield page
    page.teardown()

# test_auth.py
def test_register(orchestrator, auth_page):
    modules = orchestrator.modules.get("auth", [])
    register_module = next(m for m in modules if "register" in m.__name__)
    register_module(auth_page)
```

---

## Patrones Avanzados

### 1. Shared Context entre Servicios

```python
class MultiServiceContext:
    """Contexto compartido entre múltiples servicios."""
    
    def __init__(self):
        self.auth_page = AuthPage()
        self.social_page = SocialUserPage()
        self.data = {}  # Almacén de datos compartidos
    
    def setup(self):
        self.auth_page.setup()
        self.social_page.setup()
    
    def teardown(self):
        self.auth_page.teardown()
        self.social_page.teardown()

# Uso
context = MultiServiceContext()
context.setup()

# Auth crea usuario
register_flow.run(context.auth_page)
user_id = context.auth_page.user_id

# Social usa el ID
context.data["user_id"] = user_id
social_profile_flow.run(context)
```

### 2. Fixtures y Data Builders

```python
# utils/fixtures.py

import time
from dataclasses import dataclass

@dataclass
class UserFixture:
    email: str
    username: str
    password: str

def generate_user(suffix: int = None) -> UserFixture:
    """Generar usuario único."""
    suffix = suffix or int(time.time()) % 10000
    return UserFixture(
        email=f"user_{suffix}@test.com",
        username=f"testuser_{suffix}",
        password="StrongPass123!"
    )

# Uso en módulo
def run(auth_page: AuthPage):
    user = generate_user()
    response = auth_page.register(user.username, user.email, user.password)
```

### 3. Retry Logic y Circuit Breakers

```python
from tenacity import retry, stop_after_attempt, wait_exponential

class ResilientPage(BasePage):
    """Página con lógica de reintentos."""
    
    @retry(stop=stop_after_attempt(3), wait=wait_exponential(multiplier=1, min=4, max=10))
    def get_with_retry(self, endpoint: str):
        """GET con reintentos automáticos."""
        response = self.get(endpoint)
        if response.status >= 500:
            raise Exception(f"Server error: {response.status}")
        return response
```

### 4. Mocking y Stubs

```python
# Para tests unitarios de módulos sin Playwright

from unittest.mock import Mock, MagicMock

def test_register_flow_logic():
    """Test de lógica de negocio sin HTTP real."""
    mock_page = Mock()
    mock_page.register.return_value = Mock(
        status=201,
        json=lambda: {"data": {"id": "123"}}
    )
    
    # Ejecutar lógica del módulo
    response = register_flow.run(mock_page)
    
    assert response.status == 201
    mock_page.register.assert_called_once()
```

---

## AI Governance

### Principios de Arquitectura para Agentes IA

1. **Zero-Coupling**: El core NO debe importar nada de servicios específicos
2. **Type Hinting**: Todos los métodos públicos deben tener type hints
3. **Pydantic Models**: Todas las configuraciones y DTOs usan Pydantic
4. **Environment-Based**: Sin URLs, nombres de servicio, o strings hardcodeados
5. **Dynamic Loading**: Usar `importlib` en vez de imports estáticos cuando sea posible
6. **Protocols**: Preferir `typing.Protocol` sobre herencia abstracta

### Checklist para Agentes IA

Antes de crear código nuevo, verificar:

- [ ] ¿El nuevo código está en `services/` y no en `core/`?
- [ ] ¿Las URLs vienen de variables de entorno?
- [ ] ¿Los DTOs usan Pydantic?
- [ ] ¿Hay type hints en todos los métodos públicos?
- [ ] ¿El módulo tiene función `run(context)`?
- [ ] ¿Se ejecutó `check_deps.py` sin errores?

### Plantillas para Generación Automática

#### Plantilla de Módulo de Test

```python
# .agent/templates/test_module_template.py

from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from {{ service_module }}.{{ service_page }} import {{ ServicePage }}}

def run({{ service_var }}: '{{ ServicePage }}') -> APIResponse:
    """
    {{ description }}
    
    Args:
        {{ service_var }}: Instancia de {{ ServicePage }}
        
    Returns:
        APIResponse: Respuesta HTTP
        
    Raises:
        AssertionError: Si el flujo falla
    """
    print("Running {{ module_name }}...")
    
    # TODO: Implementar lógica de prueba
    response = {{ service_var }}.{{ method }}({{ params }})
    
    if response.ok:
        print("✓ {{ success_message }}")
    else:
        print(f"✗ Failed: {response.status}")
        raise AssertionError("{{ failure_message }}")
    
    return response
```

#### Plantilla de Service Page

```python
# .agent/templates/service_page_template.py

from verify_services.e2e.core.base_page import BasePage
from .data_schema import {{ DTOs }}
from .config import get_{{ service }}_config
from playwright.sync_api import APIResponse
from typing import Optional

class {{ ServiceName }}Page(BasePage):
    """Hub para {{ service }} service."""

    def __init__(self, playwright=None, base_url=None):
        config = get_{{ service }}_config()
        url = base_url or config.base_url
        super().__init__(url, playwright, default_headers=config.default_headers)
        
        # State
        self.current_{{ entity }}: Optional[{{ EntityDTO }}] = None
        
    def {{ action }}(self, {{ params }}) -> APIResponse:
        """{{ description }}"""
        response = self.{{ http_method }}("{{ endpoint }}", data={{ data }})
        
        if response.ok:
            data = response.json().get('data', {})
            self.current_{{ entity }} = {{ EntityDTO }}(**data)
        
        return response
```

---

## Troubleshooting

### Problema: "No module named 'verify_services'"

**Solución**: Agregar al PYTHONPATH

```python
import sys
import os
sys.path.insert(0, os.path.dirname(os.path.dirname(__file__)))
```

### Problema: "Service not found or has no modules"

**Verificar**:
1. ¿El directorio `services/{nombre}/modules/` existe?
2. ¿Los archivos `.py` tienen la función `run(context)`?
3. ¿Hay archivos `__init__.py` vacíos en los paquetes?

### Problema: Playwright timeout

**Soluciones**:
1. Aumentar timeout en `.env`: `E2E_DEFAULT_TIMEOUT=60000`
2. Verificar que el servicio está corriendo: `curl http://localhost:8085/actuator/health`
3. Verificar red: ¿está el puerto expuesto?

### Problema: Errores de deserialización

**Verificar**:
1. ¿Los DTOs Pydantic coinciden con el schema real de la API?
2. ¿La API usa snake_case vs camelCase?
3. ¿Hay campos nullable que no están marcados como `Optional`?

### Problema: Estado no persiste entre módulos

**Verificar**:
1. ¿La Service Page actualiza sus atributos después de requests exitosas?
2. ¿Los módulos reciben la misma instancia de Service Page?
3. ¿Se está llamando `teardown()` demasiado temprano?

---

## Referencia de API

### BasePage

| Método | Parámetros | Retorno | Descripción |
|--------|-----------|---------|-------------|
| `get` | `endpoint, headers=None, params=None` | `APIResponse` | GET request |
| `post` | `endpoint, data=None, headers=None` | `APIResponse` | POST request |
| `put` | `endpoint, data=None, headers=None` | `APIResponse` | PUT request |
| `patch` | `endpoint, data=None, headers=None` | `APIResponse` | PATCH request |
| `delete` | `endpoint, headers=None` | `APIResponse` | DELETE request |
| `setup` | - | `None` | Inicializar API context |
| `teardown` | - | `None` | Limpiar recursos |
| `from_config` | `config: ServiceConfig` | `BasePage` | Factory method |

### TestOrchestrator

| Método | Parámetros | Descripción |
|--------|-----------|-------------|
| `__init__` | `root_dir=None, services_path` | Constructor |
| `discover_modules` | - | Auto-descubre módulos |
| `run_service_tests` | `service_name, context` | Ejecuta tests de un servicio |
| `run_all_tests` | `context_factory` | Ejecuta todos los tests |

### ModuleLoader

| Método | Parámetros | Retorno | Descripción |
|--------|-----------|---------|-------------|
| `load_runnable_from_file` | `file_path, function_name="run"` | `Callable | None` | Carga función de archivo |
| `discover_runnables` | `root_path, pattern="*.py"` | `List[Callable]` | Descubre todas las funciones |

### ServiceConfig (Pydantic)

```python
class ServiceConfig(BaseModel):
    name: str                    # Nombre del servicio
    base_url: str               # URL base (sin trailing /)
    default_headers: Dict       # Headers por defecto
    timeout: int = 30000        # Timeout en ms
    extra: Dict                 # Config adicional
```

### TestContext (Pydantic)

```python
class TestContext(BaseModel):
    env: str = "dev"            # Environment (dev, test, prod)
    services: Dict[str, ServiceConfig]  # Configs por servicio
    metadata: Dict              # Metadatos adicionales
```

---

## Ejemplo Completo: Crear un Nuevo Flujo de Test

### Paso 1: Crear el módulo

```bash
touch verify_services/e2e/services/auth/modules/21_two_factor_flow.py
```

### Paso 2: Implementar

```python
# verify_services/e2e/services/auth/modules/21_two_factor_flow.py

from playwright.sync_api import APIResponse
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from verify_services.e2e.services.auth.auth_page import AuthPage

def run(auth_page: 'AuthPage') -> APIResponse:
    """Flujo de autenticación de dos factores."""
    print("Running 2FA flow...")
    
    # Precondición: usuario logueado
    if not auth_page.is_logged_in:
        raise ValueError("User must be logged in to enable 2FA")
    
    # Paso 1: Habilitar 2FA
    response = auth_page.post("/2fa/enable", {})
    assert response.status == 200, "Failed to enable 2FA"
    
    # Extraer secret o QR code
    data = response.json().get('data', {})
    secret = data.get('secret')
    
    # Paso 2: Verificar código TOTP (simulado)
    import pyotp
    totp = pyotp.TOTP(secret)
    code = totp.now()
    
    response = auth_page.post("/2fa/verify", {"code": code})
    assert response.ok, "Failed to verify 2FA code"
    
    # Actualizar estado
    auth_page.test_2fa_enabled = True
    auth_page.test_2fa_secret = secret
    
    print("✓ 2FA enabled and verified")
    return response
```

### Paso 3: Agregar a AuthPage (si necesita nuevos métodos)

```python
# En auth_page.py

def enable_2fa(self) -> APIResponse:
    """Enable two-factor authentication."""
    headers = {"Authorization": f"Bearer {self.auth_result.token}"}
    return self.post("/2fa/enable", {}, headers=headers)

def verify_2fa(self, code: str) -> APIResponse:
    """Verify 2FA code."""
    headers = {"Authorization": f"Bearer {self.auth_result.token}"}
    return self.post("/2fa/verify", {"code": code}, headers=headers)
```

### Paso 4: Ejecutar

```bash
# Individual
python -c "
from verify_services.e2e.services.auth.auth_page import AuthPage
from verify_services.e2e.services.auth.modules import login_flow, two_factor_flow

page = AuthPage()
page.setup()
try:
    login_flow.run(page)
    two_factor_flow.run(page)
finally:
    page.teardown()
"

# O con el orquestador completo
python verify_services/run_complete_verification.py
```

---

## Conclusión

Este framework está diseñado para ser:

1. **Extensible**: Agregar nuevos servicios requiere mínimo código boilerplate
2. **Mantenible**: Separación clara entre core genérico y lógica específica
3. **Testeable**: Fácil de unit test con mocks
4. **AI-Friendly**: Estructura clara y patrones consistentes para generación automática

Para contribuir nuevos tests o servicios, seguir siempre:
- El principio de **Zero-Coupling**
- Las convenciones de **nomenclatura**
- El checklist de **AI Governance**

---

**Mantenido por**: AI Governance Layer & Human Review  
**Última actualización**: 2026-01-30  
**Versión del Core**: 2.0.0
