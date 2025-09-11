package com.our.socialseed.auth.domain.service;

import com.our.socialseed.auth.entry.rest.dto.RegisterRequestDTO;

public interface AuthService {
    String login(String email, String password);
    String register(RegisterRequestDTO dto);
}
