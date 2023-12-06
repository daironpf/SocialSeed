package com.social.seed.controller;

import com.social.seed.model.Post;
import com.social.seed.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    @Autowired
    PostService postService;

    //region crud
    @GetMapping("/getPostById/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id){
        ResponseEntity<Post> response = postService.getPostById(id);

        return response;
    }
    //endregion
}
