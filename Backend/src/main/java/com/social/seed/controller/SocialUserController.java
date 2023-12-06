package com.social.seed.controller;

import com.social.seed.model.SocialUser;
import com.social.seed.service.SocialUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/socialUser")
public class SocialUserController {
    @Autowired
    SocialUserService socialUserService;

    //region crud
    @GetMapping("/getSocialUserById/{id}")
    public ResponseEntity<SocialUser> getSocialUserById(@PathVariable String id){
        ResponseEntity<SocialUser> response = socialUserService.getSocialUserById(id);

        return response;
    }
    //endregion
}
