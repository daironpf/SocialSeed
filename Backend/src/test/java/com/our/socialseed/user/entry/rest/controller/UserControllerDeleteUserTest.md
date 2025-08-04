# 🧪 Tests del método `UserController#deleteUser`

Este conjunto de tests verifica el comportamiento del endpoint `DELETE /api/users/{id}`, cubriendo tanto el escenario exitoso como casos de error cuando el ID no es válido o el usuario no existe.

---

## ✅ Caso exitoso

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnNoContent_whenUserIsDeletedSuccessfully` | Verifica que se retorna `204 No Content` cuando el usuario se elimina correctamente. |

---

## ❌ Casos inválidos de ID

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnBadRequest_whenIdIsNotUUID` | Retorna `400 Bad Request` si el ID proporcionado no es un UUID válido. |

---

## ❌ Casos de entidad no encontrada

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnNotFound_whenUserDoesNotExist` | Retorna `404 Not Found` si no se encuentra un usuario con el ID proporcionado. |

---

## ⚙️ Frameworks y herramientas utilizadas

- `JUnit 5`
- `Spring MockMvc`
- `Jackson ObjectMapper`
- `Mockito`
- `@WithMockUser` para simular autenticación
- `@WebMvcTest` y `@Import` para configuración aislada de capa web