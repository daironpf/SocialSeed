package com.socialseed.socialuserservice.user.entry.grpc.service;

import com.socialseed.contracts.socialuser.CreateUserRequest;
import com.socialseed.contracts.socialuser.CreateUserResponse;
import com.socialseed.contracts.socialuser.SocialUserServiceGrpc;
import com.socialseed.socialuserservice.user.application.usecase.UserUseCases;
import com.socialseed.socialuserservice.user.domain.model.User;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class SocialUserGrpcServiceImpl extends SocialUserServiceGrpc.SocialUserServiceImplBase {
    private Logger log = LoggerFactory.getLogger(SocialUserGrpcServiceImpl.class);
    private final UserUseCases userUseCases;

    public SocialUserGrpcServiceImpl(UserUseCases userUseCases) {
        this.userUseCases = userUseCases;
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserResponse> responseObserver) {
        User newuser = User.create(
                request.getUsername(),
                request.getEmail());

        User saved = userUseCases.createUser(newuser);
        // crear nodo de usuario

        log.info("usuario a crear: {}", request.toString());
        var response = CreateUserResponse.newBuilder()
                .setUserId(saved.getId().toString())
                .setMessage("200")
                .build();
        log.info("Usuario Registrado: response: {}", response.toString());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
