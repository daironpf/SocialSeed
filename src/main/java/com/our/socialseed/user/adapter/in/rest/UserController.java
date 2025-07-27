package com.our.socialseed.user.adapter.in.rest;
import com.our.socialseed.user.domain.model.User;
import com.our.socialseed.user.domain.port.in.UserService;
import com.our.socialseed.user.dto.UserDtoMapper;
import com.our.socialseed.user.dto.UserRequest;
import com.our.socialseed.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    // CREATE
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        var created = userService.createUser(UserDtoMapper.toDomain(request));
        return ResponseEntity.ok(UserDtoMapper.toResponse(created));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(UserDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // LIST
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserDtoMapper::toResponse)
                .collect(Collectors.toList());
    }
}
