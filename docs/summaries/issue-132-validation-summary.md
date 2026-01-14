# Resumen de Trabajo - Issue #132

**Título:** Add Validation Annotations to REST DTOs
**Rama:** `issue-132-dto-validation`

## Cambios realizados

Se han añadido y estandarizado las anotaciones de validación en los Objetos de Transferencia de Datos (DTOs) de los servicios de autenticación (`auth-service`) y usuario (`socialuser-service`).

### 1. auth-service
- **`LoginRequestDTO`**:
    - Convertido de `class` a `record` para mayor consistencia.
    - Añadidas validaciones: `@NotBlank`, `@Email` para el correo y `@ValidPassword` (plataforma) para la contraseña.
- **`RegisterRequestDTO`**:
    - Se activaron las validaciones comentadas.
    - Se integraron las anotaciones de plataforma: `@ValidUsername` y `@ValidPassword`.
- **`PasswordChangeRequest`**:
    - Se añadió `@ValidPassword` para garantizar que la nueva contraseña cumpla con las políticas de seguridad de la plataforma.
    - Limpieza de paquetes e imports no utilizados.

### 2. socialuser-service
- **`UserCreateRequestDTO`**:
    - Se actualizó para usar `@ValidPassword` de la plataforma en lugar de una validación manual de tamaño.
- **`UpdateUserProfileDTO`**:
    - Se verificó la presencia de validaciones para todos los campos (ID, Nombre Completo, Bio, Imagen de Perfil, Fecha de Nacimiento y Idioma).

### 3. Controlador de Autenticación
- Se actualizó `AuthController` para reflejar el cambio de `LoginRequestDTO` a `record`, usando los métodos de acceso correctos (`request.email()` y `request.password()`).

## Verificación
- Se comprobó que los controladores (`AuthController` y `UserController`) utilicen la anotación `@Valid` en los parámetros `@RequestBody` para activar el motor de validación de Spring.
- Con los cambios realizados en el `GlobalErrorHandler` anteriormente, cualquier fallo en estas validaciones devolverá ahora un error 400 estandarizado con los detalles de los campos fallidos.
