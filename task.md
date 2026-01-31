# Task: Architect Core Decoupling & AI Governance Layer

> **Issue #182**: Desacoplar el Core Engine del framework E2E y establecer una capa de Gobernanza de IA para asegurar escalabilidad y eficiencia agéntica.

---

## 📋 Estado de Progreso

- [x] **Fase 0: Capa de Gobernanza de IA & Workflow Agéntico**
- [x] **Fase 1: Auditoría y Preparación**
- [x] **Fase 2: Sistema de Configuración Genérico**
- [x] **Fase 3: Refactorizar BasePage**
- [x] **Fase 4: Refactorizar TestOrchestrator**
- [x] **Fase 5: Interfaz de Abstracciones (Protocols)**
- [x] **Fase 6: Loader Genérico & Carga Dinámica**
- [x] **Fase 7: Documentación del Core**
- [x] **Fase 8: Verificación de Dependencias (Zero-Coupling)**
- [x] **Fase 9: Adaptar Servicios Existentes (Auth)**
- [x] **Fase 10: Plantillas para Agentes (Templates)**
- [x] **Fase 11: Verificación Final y Extracción**

---

## 🛠 Detalle de Tareas

### Fase 0: Capa de Gobernanza de IA
- [ ] **0.1** Crear `.agent/rules.yaml` con reglas de arquitectura estrictas (Type hinting, Pydantic, No-coupling).
- [ ] **0.2** Crear `.agent/workflow.md` con el protocolo operativo para escaneo, actualización y generación.

### Fase 1: Auditoría y Preparación
- [x] **1.1** Verificar que no existan referencias a "SocialSeed", "Auth" o URLs hardcodeadas en `core/`.
- [x] **1.2** Documentar las dependencias actuales del core hacia services.

### Fase 2: Sistema de Configuración Genérico
- [x] **2.1** Crear `core/config.py` para cargar variables de entorno y soportar `.env`.
- [x] **2.2** Definir `core/types.py` con `ServiceConfig` y `TestContext`.

### Fase 3: Refactorizar BasePage
- [x] **3.1** Modificar `core/base_page.py` para headers configurables y factory method `from_config`.
- [x] **3.2** Crear `core/headers.py` con constantes para headers comunes.

### Fase 4: Refactorizar TestOrchestrator
- [x] **4.1** Modificar constructor para aceptar `root_dir` y `services_path`.
- [x] **4.2** Refactorizar `discover_modules()` para usar `importlib` y paths relativos.

### Fase 5: Interfaz de Abstracciones
- [x] **5.1** Crear `core/interfaces.py` con `IServicePage` y `ITestModule` usando `typing.Protocol`.

### Fase 6: Loader Genérico
- [x] **6.1** Crear `core/loaders.py` para separar la lógica de carga de la orquestación.

### Fase 7: Documentación
- [x] **7.1** Crear `core/README-core.md` con guía de uso y ejemplo de extracción.
- [x] **7.2** Asegurar docstrings en todas las clases públicas del core.

### Fase 8: Verificación de Dependencias
- [x] **8.1** Crear `core/check_deps.py` para asegurar "zero-coupling" (validar imports).
- [x] **8.2** Ejecutar y corregir cualquier violación a la regla de desacoplamiento.

### Fase 9: Actualizar Servicios Existentes
- [x] **9.1** Mover configuraciones de Auth a variables de entorno.
- [x] **9.2** Crear `services/auth/config.py` y actualizar `AuthPage` y `run_auth_tests.py`.

### Fase 10: Plantillas para Agentes
- [x] **10.1** Crear `.agent/templates/test_module_template.py`.
- [x] **10.2** Crear `.agent/templates/service_page_template.py`.

### Fase 11: Verificación Final
- [x] **11.1** Ejecutar los 12 flujos de Auth Service y verificar éxito.
- [x] **11.2** Validar que el Core sea extraíble sin errores de importación.

---

## 🎯 Criterios de Éxito
1. ✅ `core/` es 100% agnóstico (sin strings de SocialSeed).
2. ✅ Existe una capa de gobernanza funcional en `.agent/`.
3. ✅ Los 12 flujos de Auth funcionan con la nueva arquitectura.
4. ✅ `check_deps.py` confirma el desacoplamiento total.
