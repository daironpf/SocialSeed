package com.social.seed.controller;

import com.social.seed.service.HashTagService;
import com.social.seed.util.ResponseDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(HashTagControllerTest.class)
class HashTagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private HashTagControllerTest hashTagControllerTest;
    @Mock
    private HashTagService hashTagService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllHashTag() {
    }

    @Test
    void getHashTagById() throws Exception {
        // Configuración del servicio mock
        when(hashTagService.getHashTagById("1")).thenReturn("Hola desde el servicio");

        // Ejecutar la solicitud HTTP y verificar la respuesta
        mockMvc.perform(MockMvcRequestBuilders.get("/api/mensaje"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hola desde el servicio"));

        // Verificar que el método del servicio fue llamado
        verify(hashTagService, times(1)).generarMensaje();

    }

    @Test
    void testGetHashTagById() throws Exception {
        // Mock del servicio para devolver una ResponseEntity simulada
        String hashtagId = "123";
        ResponseDTO fakeResponseDTO = new ResponseDTO(HttpStatus.OK,"OK", "Datos simulados");
        ResponseEntity<Object> fakeServiceResponse = new ResponseEntity<>(fakeResponseDTO, HttpStatus.OK);

        when(hashTagService.getHashTagById(hashtagId)).thenReturn(fakeServiceResponse);

        // Llamada al controlador y verificación de la respuesta
        ResponseEntity<ResponseDTO> actualResponse = hashTagControllerTest.getHashTagById(hashtagId);

        assertThat(actualResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualResponse.getBody()).isEqualTo(fakeResponseDTO);
    }

    @Test
    void createHashTag() {
    }

    @Test
    void updateHashTag() {
    }

    @Test
    void deleteHashTag() {
    }
}