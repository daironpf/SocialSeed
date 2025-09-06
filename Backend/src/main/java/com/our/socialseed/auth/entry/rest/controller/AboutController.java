package com.our.socialseed.auth.entry.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/about")
public class AboutController {

    @GetMapping("/oo")
    public ResponseEntity<Map<String, String>> about() {
        Map<String, String> response = new HashMap<>();
        response.put("name", "SocialSeed");
        response.put("iconUrl", "https://example.com/icon.png");
        return ResponseEntity.ok(response);
    }
}

