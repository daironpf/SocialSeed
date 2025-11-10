package com.socialseed.authservice;

import com.socialseed.socialuserservice.proto.SocialUserServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserClient;

    @GetMapping("/user")
    String registerUser(@RequestParam String username, @RequestParam String password) {
        var request = com.socialseed.socialuserservice.proto.CreateUserRequest.newBuilder()
                .setUsername(username)
                .setEmail(username+"@gmail.com")
                .build();
        log.info("Create user request : {}", request);
        var response = socialUserClient.createUser(request).getMessage();
        log.info("Create user response : {}", response);
        return response.toString();
    }

}
