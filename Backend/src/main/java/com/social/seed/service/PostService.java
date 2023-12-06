package com.social.seed.service;

import com.social.seed.model.Post;
import com.social.seed.model.SocialUser;
import com.social.seed.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {
    @Autowired
    PostRepository postRepository;

    //region crud
    public ResponseEntity<Post> getPostById(String id){

        HttpStatus httpStatus = HttpStatus.OK;
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()){
            httpStatus = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<Post>(post.get(), httpStatus);
    }
    //endregion
}
