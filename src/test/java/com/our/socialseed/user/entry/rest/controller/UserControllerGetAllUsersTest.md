
# 🧪 Tests del método `UserController#getAllUsers`

Este conjunto de pruebas valida el comportamiento del endpoint `GET /api/users`, encargado de listar todos los usuarios registrados. Los tests cubren escenarios tanto exitosos como de error.

---

## ✅ Caso exitoso

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnUserList_whenUsersExist` | Verifica que se retorne una lista de usuarios (`200 OK`) cuando existen usuarios en el sistema. Se validan los campos `id`, `username`, `email` y `fullName` del primer usuario. |

---

## ❌ Casos con lista vacía

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnNoContent_whenNoUsersExist` | Retorna `204 No Content` si no hay usuarios registrados en el sistema. |

---

## ⚠️ Casos de error interno

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnInternalServerError_whenUseCaseThrowsException` | Retorna `500 Internal Server Error` si ocurre una excepción inesperada al obtener los usuarios. |

---

## ⚙️ Frameworks y herramientas utilizadas

- `JUnit 5` – Para la estructura de los tests.
- `Spring Boot WebMvcTest` – Para testear el controlador en aislamiento.
- `MockMvc` – Para simular peticiones HTTP.
- `Mockito` – Para crear mocks de los casos de uso.
- `@MockBean` – Para inyectar mocks en el contexto de Spring.
- `@WithMockUser` – Para simular autenticación.
- `Jackson ObjectMapper` – Para el manejo de JSON (implícito en `MockMvc` y Jackson).
- `@Import(MockUserUseCasesConfig.class)` – Importa una configuración de beans mockeados.
