package com.our.socialseed.auth.domain.service;

import com.our.socialseed.auth.entry.rest.dto.AuthResponseDTO;
import com.our.socialseed.auth.entry.rest.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO login(String email, String password);
    AuthResponseDTO register(RegisterRequestDTO dto);
}
