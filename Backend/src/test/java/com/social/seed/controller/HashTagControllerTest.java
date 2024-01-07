package com.social.seed.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.social.seed.controller.HashTagController;
import com.social.seed.model.HashTag;
import com.social.seed.service.HashTagService;
import com.social.seed.util.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ExtendWith(MockitoExtension.class)
//@WebMvcTest(HashTagController.class)
class HashTagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private HashTagController underTest;
    @MockBean
    private HashTagService hashTagService;

    @Autowired
    private ObjectMapper objectMapper; // Agregada instancia de ObjectMapper

    private HashTag hashTag1;

    @BeforeEach
    void setUp() {
        hashTag1 = new HashTag("1", "PrimerHashTag", 0, 0);
    }

//    @Test
    public void testGetHashTagById() throws Exception {
        // Configurar el comportamiento del servicio mock
        when(hashTagService.getHashTagById(hashTag1.getId())).thenReturn(ResponseEntity.ok(hashTag1));

        // Realizar la solicitud HTTP y verificar la respuesta
        MvcResult result = mockMvc.perform(get("/api/v0.0.1/hashTag/getHashTagById/{id}", hashTag1.getId()))
                .andExpect(status().isOk())
                .andReturn();

        // Convertir la respuesta a un objeto HashTag
        HashTag responseHashTag = objectMapper.readValue(result.getResponse().getContentAsString(), HashTag.class);

        // Verificar que el servicio fue llamado
        verify(hashTagService, times(1)).getHashTagById(hashTag1.getId());

        // Asegurarse de que el HashTag obtenido coincida con el esperado
        assertThat(responseHashTag.getId()).isEqualTo(hashTag1.getId());
        assertThat(responseHashTag.getName()).isEqualTo("TestHashTag");
    }
}
