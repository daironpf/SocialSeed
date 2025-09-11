# 🧪 Tests del método `UserController#getUserById`

Este conjunto de tests verifica el comportamiento del endpoint `GET /api/users/{id}`, cubriendo tanto el escenario exitoso como distintos casos de error relacionados con IDs inválidos o usuarios no encontrados.

---

## ✅ Caso exitoso

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnUser_whenUserExists` | Verifica que se retorna `200 OK` con los datos del usuario cuando el ID corresponde a un usuario existente. |

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

## 💥 Casos de error inesperado

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnInternalServerError_whenUseCaseFails` | Retorna `500 Internal Server Error` si ocurre una excepción inesperada durante la ejecución del caso de uso. |

---

## ⚙️ Frameworks y herramientas utilizadas

- `JUnit 5`
- `Spring MockMvc`
- `Jackson ObjectMapper`
- `Mockito`
- `@WithMockUser` para simular autenticación
- `@WebMvcTest` y `@Import` para configuración aislada de la capa web

