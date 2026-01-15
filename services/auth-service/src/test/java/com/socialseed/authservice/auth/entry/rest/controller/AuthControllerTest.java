package com.socialseed.authservice.auth.entry.rest.controller;

import com.socialseed.authservice.auth.application.usecase.AuthUseCases;
import com.socialseed.authservice.auth.entry.rest.dto.LogoutRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    private AuthUseCases authUseCases;

    @Autowired
    private ObjectMapper objectMapper;

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
