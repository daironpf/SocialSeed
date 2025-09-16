package com.our.socialseed.about.entry.rest.controller;

import com.our.socialseed.about.entry.rest.dto.AboutResponseDTO;
import com.our.socialseed.user.application.usecase.UserUseCases;
import com.our.socialseed.user.entry.rest.dto.UserResponseDTO;
import com.our.socialseed.user.entry.rest.mapper.UserRestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/about")
public class AboutController {
    private final AboutUseCases userUseCases;

    public UserController(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }

    // GET ABOUT
    @GetMapping("")
    public ResponseEntity<AboutResponseDTO> getAbout() {
        return


        Map<String, String> response = new HashMap<>();
        response.put("name", "SocialSeed");
        response.put("iconUrl", "https://example.com/icon.png");
        return ResponseEntity.ok(response);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        return userUseCases.getUserById().execute(id)
                .map(user -> ResponseEntity.ok(UserRestMapper.toResponse(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}