| Carpeta / Archivo            | Rol                                                     |
| ---------------------------- | ------------------------------------------------------- |
| `domain/model`               | Define la entidad de dominio `User`                     |
| `domain/repository`          | Interfaz que define los métodos que el dominio necesita |
| `application/usecase`        | Casos de uso como `CreateUser`, `FindUserById`, etc.    |
| `infrastructure/persistence` | Entidad Neo4j + implementación concreta del repositorio |
| `infrastructure/mapper`      | Traduce entre entidad persistente y modelo de dominio   |
| `entry/rest`                 | Controlador que maneja las peticiones HTTP              |
| `infrastructure/persistence/entity/` | Entidad que representa al usuario en la base de datos (Neo4j)             |
| `infrastructure/persistence/mapper/` | Mapper que convierte entre `UserNeo4jEntity` y `User` (modelo de dominio) |
| `entry/rest/dto/`                    | DTOs usados para enviar/recibir datos desde la API                        |
| `entry/rest/mapper/`                 | Mapper que convierte entre DTOs y el modelo de dominio                    |
| `infrastructure/persistence/entity/` | Entidad que representa al usuario en la base de datos (Neo4j)             |
| `infrastructure/persistence/mapper/` | Mapper que convierte entre `UserNeo4jEntity` y `User` (modelo de dominio) |
| `entry/rest/dto/`                    | DTOs usados para enviar/recibir datos desde la API                        |
| `entry/rest/mapper/`                 | Mapper que convierte entre DTOs y el modelo de dominio                    |

---

### 🧱 Estructura de las carpetas

```plaintext
socialseed/
└── user/
    ├── domain/
    │   ├── model/
    │   │   └── User.java
    │   ├── repository/
    │   │   └── UserRepository.java # Domain interface
    │   └── service/
    │       └── UserService.java # Domain logic interface (optional if needed)
    ├── application/
    │   └── usecase/
    │       ├── CreateUser.java
    │       ├── FindUserById.java
    │       └── UpdateUser.java
    ├── infrastructure/
    │   ├── persistence/
    │   │   ├── entity/
    │   │   │   └── UserNeo4jEntity.java
    │   │   ├── SpringUserRepository.java  # Implements UserRepository using Spring Data Neo4j
    │   │   └── mapper/
    │   │       └── UserPersistenceMapper.java   # Entity <-> Domain
    └── entry/
        ├── rest/
        │   ├── controller/
        │   │   └── UserController.java
        │   ├── dto/
        │   │   ├── UserRequestDTO.java
        │   │   └── UserResponseDTO.java
        │   └── mapper/
        │       └── UserRestMapper.java          # DTO <-> Domain
```

---



---

### Ejemplo de responsabilidades

* `UserNeo4jEntity.java`: Anotada con `@Node`, campos como se guardan en Neo4j.
* `User.java`: Modelo de dominio limpio, sin dependencias de frameworks.
* `UserRequestDTO.java`: Lo que espera recibir el API (por ejemplo, `username`, `email`, etc.).
* `UserResponseDTO.java`: Lo que devuelve el API.
* `UserRestMapper.java`: Convierte entre `User <-> UserDTO`.
* `UserPersistenceMapper.java`: Convierte entre `User <-> UserNeo4jEntity`.

---

