* Login (`/auth/login`)
* Registro (`/auth/register`)
* Verificación de credenciales con `UserRepository` y `PasswordEncoder`
* Generación y validación de **JWTs**
* Seguridad basada en roles (`ROLE_USER`, `ROLE_ADMIN`, etc.)

---

## 📦 Estructura de carpetas

```plaintext
auth/
├── application/
│   └── usecase/
│       ├── AuthenticateUser.java
│       └── RegisterUser.java
├── domain/
│   └── service/
│       └── AuthService.java
├── entry/
│   └── rest/
│       ├── controller/
│       │   └── AuthController.java
│       ├── dto/
│       │   ├── LoginRequestDTO.java
│       │   ├── RegisterRequestDTO.java
│       │   └── AuthResponseDTO.java
│       └── mapper/
│           └── AuthRestMapper.java
├── infrastructure/
│   └── security/
│       ├── JWTProvider.java
│       ├── JWTAuthenticationFilter.java
│       └── SecurityConfig.java
```
