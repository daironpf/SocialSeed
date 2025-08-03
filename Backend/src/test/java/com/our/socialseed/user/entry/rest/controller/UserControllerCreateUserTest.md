# 🧪 Tests del método `UserController#createUser`

Este conjunto de tests verifica el comportamiento esperado del endpoint `POST /api/users`, validando tanto el caso exitoso como todos los escenarios de validación fallida según las reglas de negocio aplicadas en el DTO `UserCreateRequestDTO`.

---

## ✅ Caso exitoso

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnOk_whenUserIsCreatedSuccessfully` | Verifica que se retorna `200 OK` y el cuerpo con los datos correctos cuando el usuario se crea exitosamente. |

---

## ❌ Validaciones de campo: `username`

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnBadRequest_whenUsernameIsNull` | Retorna `400 Bad Request` si el campo `username` es `null`. |
| `shouldReturnBadRequest_whenUsernameIsBlank` | Retorna `400 Bad Request` si el campo `username` es una cadena vacía. |
| `shouldReturnBadRequest_whenUsernameExceedsMaxLength` | Retorna `400 Bad Request` si el campo `username` supera los 30 caracteres. |

---

## ❌ Validaciones de campo: `email`

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnBadRequest_whenEmailIsNull` | Retorna `400 Bad Request` si el campo `email` es `null`. |
| `shouldReturnBadRequest_whenEmailIsBlank` | Retorna `400 Bad Request` si el campo `email` está vacío. |
| `shouldReturnBadRequest_whenEmailIsInvalid` | Retorna `400 Bad Request` si el formato del campo `email` no es válido. |

---

## ❌ Validaciones de campo: `password`

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnBadRequest_whenPasswordIsNull` | Retorna `400 Bad Request` si el campo `password` es `null`. |
| `shouldReturnBadRequest_whenPasswordIsBlank` | Retorna `400 Bad Request` si el campo `password` está vacío. |
| `shouldReturnBadRequest_whenPasswordTooShort` | Retorna `400 Bad Request` si el `password` tiene menos de 6 caracteres. |
| `shouldReturnBadRequest_whenPasswordTooLong` | Retorna `400 Bad Request` si el `password` tiene más de 60 caracteres. |

---

## ❌ Validaciones de campo: `fullName`

| Test method | Descripción |
|-------------|-------------|
| `shouldReturnBadRequest_whenFullNameIsNull` | Retorna `400 Bad Request` si el campo `fullName` es `null`. |
| `shouldReturnBadRequest_whenFullNameIsBlank` | Retorna `400 Bad Request` si el campo `fullName` está vacío. |
| `shouldReturnBadRequest_whenFullNameExceedsMaxLength` | Retorna `400 Bad Request` si el campo `fullName` supera los 100 caracteres. |

---

## ⚙️ Frameworks y herramientas utilizadas

- `JUnit 5`
- `Spring MockMvc`
- `Jackson ObjectMapper`
- `Mockito`
- `@WithMockUser` para simular autenticación
- `@WebMvcTest` y `@Import` para configuración aislada de capa web