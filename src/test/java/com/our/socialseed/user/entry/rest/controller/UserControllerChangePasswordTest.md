# 🧪 Tests del método `UserController#changePassword`

Este conjunto de tests valida el comportamiento del endpoint `POST /api/users/{id}/change-password`, que permite a un usuario autenticado cambiar su contraseña actual por una nueva. Se testean tanto los casos exitosos como la respuesta ante errores inesperados desde la capa de aplicación.

---

## ✅ Caso exitoso

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnNoContent_whenPasswordChangedSuccessfully` | Verifica que se retorna `204 No Content` cuando la contraseña del usuario es cambiada correctamente. |

---

## ❌ Manejo de errores

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnInternalServerError_whenUseCaseThrowsException` | Retorna `500 Internal Server Error` si el caso de uso lanza una excepción inesperada durante el cambio de contraseña. |

---

## ⚙️ Frameworks y herramientas utilizadas

- `JUnit 5`
- `Spring MockMvc`
- `Mockito` para mocks y stubbing
- `@WithMockUser` para simular un usuario autenticado
- `@WebMvcTest` y `@Import` para configurar tests aislados de la capa web
- `SecurityMockMvcRequestPostProcessors.csrf()` para añadir token CSRF requerido por Spring Security
