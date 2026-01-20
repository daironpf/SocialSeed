package com.socialseed.authservice.auth.entry.grpc.config;

import com.socialseed.contracts.socialuser.SocialUserServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

import static com.socialseed.contracts.socialuser.SocialUserServiceGrpc.newBlockingStub;

@Configuration
public class GrpcClientConfig {

    /**
     * Bean que expone el stub gRPC para comunicarse con el microservicio SocialUserService.
     * Se puede inyectar en cualquier servicio Spring.
     */
    @Bean
    public SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserClient(GrpcChannelFactory channelFactory) {
        return newBlockingStub(channelFactory.createChannel("user"));
        // "user" es el nombre lógico del servicio, configurado en application.yml
    }
}
