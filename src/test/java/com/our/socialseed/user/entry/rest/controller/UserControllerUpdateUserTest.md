# 🧪 Tests del método `UserController#updateUser`

Este conjunto de tests verifica el comportamiento esperado del endpoint `PUT /api/users/{id}`, validando tanto el caso exitoso como los escenarios de error por validaciones y errores internos del servidor. Las validaciones están basadas en las reglas definidas en el DTO `UserUpdateRequestDTO`.

---

## ✅ Caso exitoso

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnNoContent_whenUserUpdatedSuccessfully` | Verifica que se retorna `204 No Content` cuando el usuario se actualiza correctamente con datos válidos. |

---

## ❌ Validaciones de entrada (400 Bad Request)

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnBadRequest_whenRequestValidationFails` | Verifica que se retorna `400 Bad Request` cuando el `username` es solo espacios, el `email` tiene formato inválido y el `fullName` es `null`. También verifica que no se llama al caso de uso. |

---

## 💥 Error interno (500 Internal Server Error)

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnInternalServerError_whenUseCaseFails` | Verifica que se retorna `500 Internal Server Error` cuando el caso de uso lanza una excepción inesperada. |

---

## ⚙️ Frameworks y herramientas utilizadas

- `JUnit 5`
- `Spring MockMvc`
- `Jackson ObjectMapper`
- `Mockito`
- `@WithMockUser` para simular autenticación
- `@WebMvcTest` y `@Import(MockUserUseCasesConfig.class)` para configurar el entorno de test aislado
- Validación con anotaciones de Bean Validation (`@NotBlank`, `@Email`, `@Size`) en `UserUpdateRequestDTO`
