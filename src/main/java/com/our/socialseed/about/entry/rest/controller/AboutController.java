package com.our.socialseed.about.entry.rest.controller;

import com.our.socialseed.about.application.usecase.AboutUseCases;
import com.our.socialseed.about.entry.rest.dto.AboutResponseDTO;
import com.our.socialseed.about.entry.rest.mapper.AboutRestMapper;
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
    private final AboutUseCases aboutUseCases;

    public AboutController(AboutUseCases aboutUseCases) {
        this.aboutUseCases = aboutUseCases;
    }

    // GET ABOUT
    @GetMapping("")
    public ResponseEntity<AboutResponseDTO> getAboutInfo() {
        return aboutUseCases.getAbout().execute()
                .map(about -> ResponseEntity.ok(AboutRestMapper.toResponse(about)))
                .orElse(ResponseEntity.notFound().build());
    }
}