package com.socialseed.authservice.auth.entry.rest.controller;

import com.socialseed.authservice.auth.application.usecase.AuthUseCases;
import com.socialseed.authservice.auth.domain.model.AuthResult;
import com.socialseed.authservice.auth.entry.rest.dto.LogoutRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // CAMBIO AQUÍ: Reemplazamos @MockBean por @MockitoBean
    @MockitoBean
    private AuthUseCases authUseCases;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void shouldLoginSuccessfully() throws Exception {
        com.socialseed.authservice.auth.entry.rest.dto.LoginRequestDTO loginRequest = 
            new com.socialseed.authservice.auth.entry.rest.dto.LoginRequestDTO("test@example.com", "Password123!");
        
        org.mockito.BDDMockito.given(authUseCases.login(org.mockito.ArgumentMatchers.anyString(), 
                org.mockito.ArgumentMatchers.anyString(), 
                org.mockito.ArgumentMatchers.anyString()))
            .willReturn(new AuthResult("token", "refresh", java.util.Set.of("ROLE_USER")));
        
        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        verify(authUseCases).login(org.mockito.ArgumentMatchers.eq("test@example.com"), 
                                    org.mockito.ArgumentMatchers.eq("Password123!"), 
                                    org.mockito.ArgumentMatchers.anyString());
    }

    @Test
    @WithMockUser
    void shouldLogoutSuccessfully() throws Exception {
        LogoutRequestDTO logoutRequest = new LogoutRequestDTO("valid-refresh-token");
        String accessToken = "Bearer valid-access-token";

        mockMvc.perform(post("/auth/logout")
                        .with(csrf())
                        .header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutRequest)))
                .andExpect(status().isNoContent());

        verify(authUseCases).logout(accessToken, "valid-refresh-token");
    }
}