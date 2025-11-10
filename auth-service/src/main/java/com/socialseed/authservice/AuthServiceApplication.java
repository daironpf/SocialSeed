package com.socialseed.authservice;

import com.socialseed.socialuserservice.proto.SocialUserServiceGrpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.client.GrpcChannelFactory;

import static com.socialseed.socialuserservice.proto.SocialUserServiceGrpc.newBlockingStub;


@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    SocialUserServiceGrpc.SocialUserServiceBlockingStub userChannels(GrpcChannelFactory channels) {
        return newBlockingStub(channels.createChannel("user"));
    }

}
