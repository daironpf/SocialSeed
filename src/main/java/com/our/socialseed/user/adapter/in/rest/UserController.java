package com.our.socialseed.user.adapter.in.rest;
import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.port.in.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/*
✅ Paso 5: Adaptador de entrada REST: UserController
Este adaptador:
Recibe las peticiones HTTP (@RestController)
Usa el caso de uso (UserService) vía inyección
Expone el API REST para crear y consultar usuarios

💡 Posibles mejoras futuras
Validar datos de entrada (con DTOs y @Valid)
Mapear errores y excepciones
Usar DTOs para separar el dominio del API
Pero por ahora, para mantener el enfoque limpio y directo, trabajamos con la entidad User.
 */

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User created = userService.createUser(user);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
