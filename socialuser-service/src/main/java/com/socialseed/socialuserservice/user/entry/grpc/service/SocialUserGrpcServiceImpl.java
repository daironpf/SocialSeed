package com.socialseed.socialuserservice.user.entry.grpc.service;

import com.socialseed.socialuserservice.proto.CreateUserReply;
import com.socialseed.socialuserservice.proto.CreateUserRequest;
import com.socialseed.socialuserservice.proto.SocialUserServiceGrpc;
import com.socialseed.socialuserservice.user.application.usecase.UserUseCases;
import com.socialseed.socialuserservice.user.domain.model.User;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

import java.util.Random;

@GrpcService
public class SocialUserGrpcServiceImpl extends SocialUserServiceGrpc.SocialUserServiceImplBase {
    private Logger log = LoggerFactory.getLogger(SocialUserGrpcServiceImpl.class);
    private final UserUseCases userUseCases;

    public SocialUserGrpcServiceImpl(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserReply> responseObserver) {
        User newuser = User.builder()
                .id(null)
                .username(request.getUsername())
                .email(request.getEmail())
                .build();

        User saved = userUseCases.createUser().execute(newuser);
        // crear nodo de usuario

        log.info("usuario a crear Email: {}", request.getEmail());
        log.info("usuario a crear UserName: {}", request.getUsername());
        var response  = CreateUserReply.newBuilder()
                .setUserId(saved.getId().toString())
                .setMessage("200")
                .build();
        log.info("Usuario Registrado: response: {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
