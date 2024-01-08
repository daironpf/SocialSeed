package com.social.seed.service;

import com.social.seed.model.HashTag;
import com.social.seed.repository.HashTagRepository;
import com.social.seed.util.ResponseDTO;
import com.social.seed.util.ResponseService;
import com.social.seed.util.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HashTagServiceTest {

    @InjectMocks
    private HashTagService underTest;
    @Mock
    private HashTagRepository hashTagRepository;
    @Mock
    private ResponseService responseService;
    @Mock
    private ValidationService validationService;

    private HashTag hashTag1;

    @BeforeEach
    void setUp() {
        hashTag1 = new HashTag("1", "PrimerHashTag", 0, 0);
    }

    @Test
    void shouldReturnHashTagDetailsWhenGettingById() {
        // Arrange
        // Hacer que el responseService ejecute el código real para successResponse
        doCallRealMethod().when(responseService).successResponse(any());
        when(hashTagRepository.findById(hashTag1.getId())).thenReturn(Optional.of(hashTag1));

        // Act
        ResponseEntity<Object> responseEntity = underTest.getHashTagById(hashTag1.getId());

        // Assert
        verify(hashTagRepository, times(1)).findById(hashTag1.getId());

        assertThat(responseEntity)
                .isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isEqualTo(HttpStatus.OK);

        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
        Optional<HashTag> nuevo = (Optional<HashTag>) response.response();

        assertThat(nuevo.map(HashTag::getName)).isEqualTo(Optional.of(hashTag1.getName()));
        assertThat(nuevo.map(HashTag::getId)).isEqualTo(Optional.of(hashTag1.getId()));
    }

//    @Test
//    void shouldReturnNotFoundWhenGettingNonExistentHashTagById() {
//        // Arrange
//        doCallRealMethod().when(responseService).hashTagNotFoundResponse(any());
//        when(hashTagRepository.findById("7")).thenReturn(Optional.empty());
//
//        // Act
//        ResponseEntity<Object> responseEntity = underTest.getHashTagById("7");
//
//        // Assert
//        verify(hashTagRepository, times(1)).findById("7");
//
//        assertThat(responseEntity)
//                .isNotNull()
//                .extracting(ResponseEntity::getStatusCode)
//                .isEqualTo(HttpStatus.NOT_FOUND);
//
//        ResponseDTO response = (ResponseDTO) responseEntity.getBody();
//
//        assertThat(response.status()).isEqualTo(HttpStatus.NOT_FOUND);
//        assertThat(response.response()).isNull();
//        assertThat(response.message()).isEqualTo(String.format("Hashtag not found with ID: %s", "7"));
//    }


}
