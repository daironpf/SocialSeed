package com.social.seed.controller;

import com.social.seed.model.HashTag;
import com.social.seed.service.HashTagService;
import com.social.seed.util.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/hashTag")
public class HashTagController {
    @Autowired
    HashTagService hashTagService;
    //region gets
    @GetMapping("/getAllHashTag")
    public ResponseEntity<ResponseDTO> getAllHashTag(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.status(OK)
                .body(hashTagService.getAllHashTag(page, size)
                        .map(hashTags -> new ResponseDTO(OK, hashTags, "Successful"))
                        .orElse(new ResponseDTO(NOT_FOUND, "Error", "No hashTags available")));
    }
    //endregion

    //region CRUD
    @GetMapping("/getHashTagById/{id}")
    public ResponseEntity<ResponseDTO> getHashTagById(@PathVariable String id) {

        return ResponseEntity.status(OK)
                .body(hashTagService.getHashTagById(id)
                        .map(response -> new ResponseDTO(OK, response, "Successful"))
                        .orElse(new ResponseDTO(NOT_FOUND, "Error", String.format("The HashTag with the id [ %s ] was not found", id))));
    }

    @PostMapping("/createHashTag")
    public ResponseEntity<ResponseDTO> createHashTag(
            @RequestBody HashTag newHashTag){

        ResponseEntity<Object> responseCreate = hashTagService.createNewHashTag(newHashTag);
        HttpStatus status = (HttpStatus) responseCreate.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case CREATED -> new ResponseDTO(status, responseCreate.getBody(), "The HashTag was Created");
            case CONFLICT -> new ResponseDTO(status, "Error", (String) responseCreate.getBody());
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @PutMapping("/updateHashTag")
    public ResponseEntity<ResponseDTO> updateHashTag(
            @RequestBody HashTag updateHashTag) {

        ResponseEntity<HashTag> responseUpdate  = hashTagService.updateHashTag(updateHashTag);
        HttpStatus status = (HttpStatus) responseUpdate.getStatusCode();
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, responseUpdate.getBody(), String.format("The HashTag with the id [ %s ] was Updated", updateHashTag.getId()));
            case NOT_FOUND -> new ResponseDTO(status, "Error", String.format("The HashTag with the id [ %s ] was not found", updateHashTag.getId()));
            case CONFLICT -> new ResponseDTO(status, "Error", String.format("The HashTag with the name [ %s ] already exists ", updateHashTag.getName()));
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    @DeleteMapping("/deleteHashTag/{id}")
    public ResponseEntity<ResponseDTO> deleteHashTag(
            @PathVariable String id) {

        HttpStatus status = hashTagService.deleteHashTag(id);
        ResponseDTO responseDTO = switch (status) {
            case OK -> new ResponseDTO(status, "Successful", String.format("The HashTag with the id [ %s ] was Deleted", id));
            case NOT_FOUND -> new ResponseDTO(status, "Error", String.format("The HashTag with the id [ %s ] was not found", id));
            default -> new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Error", "Unexpected error");
        };

        return ResponseEntity.status(status).body(responseDTO);
    }

    //endregion
}
