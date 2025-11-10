package com.socialseed.socialuserservice.user.entry.grpc.service;

import com.socialseed.socialuserservice.proto.CreateUserReply;
import com.socialseed.socialuserservice.proto.CreateUserRequest;
import com.socialseed.socialuserservice.proto.SocialUserServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.grpc.server.service.GrpcService;

import java.util.Random;

@GrpcService
public class SocialUserGrpcServiceImpl extends SocialUserServiceGrpc.SocialUserServiceImplBase {
    private Logger log = LoggerFactory.getLogger(SocialUserGrpcServiceImpl.class);
    private final Random rand =  new Random();

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserReply> responseObserver) {
        log.info("usuario a crear Email: {}", request.getEmail());
        log.info("usuario a crear UserName: {}", request.getUsername());
        var response  = CreateUserReply.newBuilder()
                .setUserId("id: "+rand.toString())
                .setMessage("usuario creado")
                .build();
        log.info("Usuario Registrado: response: {}", response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
