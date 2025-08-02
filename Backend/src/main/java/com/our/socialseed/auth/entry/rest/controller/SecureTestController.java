package com.our.socialseed.auth.entry.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secure")
public class SecureTestController {

    @GetMapping
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("✅ Access granted to secure endpoint.");
    }
}

