package com.social.seed.controller;

import com.social.seed.service.SocialUserInterestInHashTagService;
import com.social.seed.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interestsInHashTagBySocialUser")
public class SocialUserInterestInHashTagController {
    //region dependencies
    @Autowired
    SocialUserInterestInHashTagService socialUserInterestInHashTagService;
    //endregion


    @PostMapping("/addInterest/{idHashTag}")
    public ResponseEntity<ResponseDTO> addInterest(
            @RequestHeader("userId") String idUserRequest,
            @PathVariable String idHashTag){

        ResponseEntity<Object> response = socialUserInterestInHashTagService.addInterest(idUserRequest, idHashTag);
        HttpStatus status = (HttpStatus) response.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", (String) response.getBody());
            case CONFLICT,NOT_FOUND -> new ResponseDTO(status, "Error", (String) response.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

//    addInterest(user, hashtag): Registra el interés del usuario en un hashtag.
//    removeInterest(user, hashtag): Elimina el interés del usuario en un hashtag.
//    getUserInterests(userId): Obtiene la lista de intereses de un usuario.
//    getPopularHashtags(): Obtiene una lista de hashtags populares basada en la interacción de los usuarios.

}
