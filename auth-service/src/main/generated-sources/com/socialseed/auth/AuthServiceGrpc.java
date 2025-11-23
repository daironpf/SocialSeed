package com.socialseed.auth;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class AuthServiceGrpc {

  private AuthServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "com.socialseed.auth.AuthService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.LoginRequest,
      com.socialseed.auth.AuthServiceOuterClass.LoginResponse> getLoginMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Login",
      requestType = com.socialseed.auth.AuthServiceOuterClass.LoginRequest.class,
      responseType = com.socialseed.auth.AuthServiceOuterClass.LoginResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.LoginRequest,
      com.socialseed.auth.AuthServiceOuterClass.LoginResponse> getLoginMethod() {
    io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.LoginRequest, com.socialseed.auth.AuthServiceOuterClass.LoginResponse> getLoginMethod;
    if ((getLoginMethod = AuthServiceGrpc.getLoginMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getLoginMethod = AuthServiceGrpc.getLoginMethod) == null) {
          AuthServiceGrpc.getLoginMethod = getLoginMethod =
              io.grpc.MethodDescriptor.<com.socialseed.auth.AuthServiceOuterClass.LoginRequest, com.socialseed.auth.AuthServiceOuterClass.LoginResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Login"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.socialseed.auth.AuthServiceOuterClass.LoginRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.socialseed.auth.AuthServiceOuterClass.LoginResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("Login"))
              .build();
        }
      }
    }
    return getLoginMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.RefreshRequest,
      com.socialseed.auth.AuthServiceOuterClass.LoginResponse> getRefreshMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Refresh",
      requestType = com.socialseed.auth.AuthServiceOuterClass.RefreshRequest.class,
      responseType = com.socialseed.auth.AuthServiceOuterClass.LoginResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.RefreshRequest,
      com.socialseed.auth.AuthServiceOuterClass.LoginResponse> getRefreshMethod() {
    io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.RefreshRequest, com.socialseed.auth.AuthServiceOuterClass.LoginResponse> getRefreshMethod;
    if ((getRefreshMethod = AuthServiceGrpc.getRefreshMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getRefreshMethod = AuthServiceGrpc.getRefreshMethod) == null) {
          AuthServiceGrpc.getRefreshMethod = getRefreshMethod =
              io.grpc.MethodDescriptor.<com.socialseed.auth.AuthServiceOuterClass.RefreshRequest, com.socialseed.auth.AuthServiceOuterClass.LoginResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Refresh"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.socialseed.auth.AuthServiceOuterClass.RefreshRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.socialseed.auth.AuthServiceOuterClass.LoginResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("Refresh"))
              .build();
        }
      }
    }
    return getRefreshMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest,
      com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse> getIntrospectMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Introspect",
      requestType = com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest.class,
      responseType = com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest,
      com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse> getIntrospectMethod() {
    io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest, com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse> getIntrospectMethod;
    if ((getIntrospectMethod = AuthServiceGrpc.getIntrospectMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getIntrospectMethod = AuthServiceGrpc.getIntrospectMethod) == null) {
          AuthServiceGrpc.getIntrospectMethod = getIntrospectMethod =
              io.grpc.MethodDescriptor.<com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest, com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Introspect"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("Introspect"))
              .build();
        }
      }
    }
    return getIntrospectMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest,
      com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse> getValidateCredentialsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateCredentials",
      requestType = com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest.class,
      responseType = com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest,
      com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse> getValidateCredentialsMethod() {
    io.grpc.MethodDescriptor<com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest, com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse> getValidateCredentialsMethod;
    if ((getValidateCredentialsMethod = AuthServiceGrpc.getValidateCredentialsMethod) == null) {
      synchronized (AuthServiceGrpc.class) {
        if ((getValidateCredentialsMethod = AuthServiceGrpc.getValidateCredentialsMethod) == null) {
          AuthServiceGrpc.getValidateCredentialsMethod = getValidateCredentialsMethod =
              io.grpc.MethodDescriptor.<com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest, com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateCredentials"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new AuthServiceMethodDescriptorSupplier("ValidateCredentials"))
              .build();
        }
      }
    }
    return getValidateCredentialsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AuthServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthServiceStub>() {
        @java.lang.Override
        public AuthServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthServiceStub(channel, callOptions);
        }
      };
    return AuthServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static AuthServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthServiceBlockingV2Stub>() {
        @java.lang.Override
        public AuthServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return AuthServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AuthServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthServiceBlockingStub>() {
        @java.lang.Override
        public AuthServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthServiceBlockingStub(channel, callOptions);
        }
      };
    return AuthServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AuthServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AuthServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AuthServiceFutureStub>() {
        @java.lang.Override
        public AuthServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AuthServiceFutureStub(channel, callOptions);
        }
      };
    return AuthServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * Login with email/username + password
     * </pre>
     */
    default void login(com.socialseed.auth.AuthServiceOuterClass.LoginRequest request,
        io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.LoginResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getLoginMethod(), responseObserver);
    }

    /**
     * <pre>
     * Refresh tokens
     * </pre>
     */
    default void refresh(com.socialseed.auth.AuthServiceOuterClass.RefreshRequest request,
        io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.LoginResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRefreshMethod(), responseObserver);
    }

    /**
     * <pre>
     * Introspect a token (returns claims) — internal use
     * </pre>
     */
    default void introspect(com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest request,
        io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getIntrospectMethod(), responseObserver);
    }

    /**
     * <pre>
     * Validate credentials (used if socialuser-service needs an immediate check)
     * </pre>
     */
    default void validateCredentials(com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest request,
        io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateCredentialsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AuthService.
   */
  public static abstract class AuthServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AuthServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AuthService.
   */
  public static final class AuthServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AuthServiceStub> {
    private AuthServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Login with email/username + password
     * </pre>
     */
    public void login(com.socialseed.auth.AuthServiceOuterClass.LoginRequest request,
        io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.LoginResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Refresh tokens
     * </pre>
     */
    public void refresh(com.socialseed.auth.AuthServiceOuterClass.RefreshRequest request,
        io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.LoginResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRefreshMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Introspect a token (returns claims) — internal use
     * </pre>
     */
    public void introspect(com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest request,
        io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getIntrospectMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Validate credentials (used if socialuser-service needs an immediate check)
     * </pre>
     */
    public void validateCredentials(com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest request,
        io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateCredentialsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AuthService.
   */
  public static final class AuthServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<AuthServiceBlockingV2Stub> {
    private AuthServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * Login with email/username + password
     * </pre>
     */
    public com.socialseed.auth.AuthServiceOuterClass.LoginResponse login(com.socialseed.auth.AuthServiceOuterClass.LoginRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Refresh tokens
     * </pre>
     */
    public com.socialseed.auth.AuthServiceOuterClass.LoginResponse refresh(com.socialseed.auth.AuthServiceOuterClass.RefreshRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getRefreshMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Introspect a token (returns claims) — internal use
     * </pre>
     */
    public com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse introspect(com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getIntrospectMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Validate credentials (used if socialuser-service needs an immediate check)
     * </pre>
     */
    public com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse validateCredentials(com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getValidateCredentialsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service AuthService.
   */
  public static final class AuthServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AuthServiceBlockingStub> {
    private AuthServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Login with email/username + password
     * </pre>
     */
    public com.socialseed.auth.AuthServiceOuterClass.LoginResponse login(com.socialseed.auth.AuthServiceOuterClass.LoginRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getLoginMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Refresh tokens
     * </pre>
     */
    public com.socialseed.auth.AuthServiceOuterClass.LoginResponse refresh(com.socialseed.auth.AuthServiceOuterClass.RefreshRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRefreshMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Introspect a token (returns claims) — internal use
     * </pre>
     */
    public com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse introspect(com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getIntrospectMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Validate credentials (used if socialuser-service needs an immediate check)
     * </pre>
     */
    public com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse validateCredentials(com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateCredentialsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AuthService.
   */
  public static final class AuthServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AuthServiceFutureStub> {
    private AuthServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AuthServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AuthServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Login with email/username + password
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.socialseed.auth.AuthServiceOuterClass.LoginResponse> login(
        com.socialseed.auth.AuthServiceOuterClass.LoginRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getLoginMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Refresh tokens
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.socialseed.auth.AuthServiceOuterClass.LoginResponse> refresh(
        com.socialseed.auth.AuthServiceOuterClass.RefreshRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRefreshMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Introspect a token (returns claims) — internal use
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse> introspect(
        com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getIntrospectMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Validate credentials (used if socialuser-service needs an immediate check)
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse> validateCredentials(
        com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateCredentialsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_LOGIN = 0;
  private static final int METHODID_REFRESH = 1;
  private static final int METHODID_INTROSPECT = 2;
  private static final int METHODID_VALIDATE_CREDENTIALS = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LOGIN:
          serviceImpl.login((com.socialseed.auth.AuthServiceOuterClass.LoginRequest) request,
              (io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.LoginResponse>) responseObserver);
          break;
        case METHODID_REFRESH:
          serviceImpl.refresh((com.socialseed.auth.AuthServiceOuterClass.RefreshRequest) request,
              (io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.LoginResponse>) responseObserver);
          break;
        case METHODID_INTROSPECT:
          serviceImpl.introspect((com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest) request,
              (io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse>) responseObserver);
          break;
        case METHODID_VALIDATE_CREDENTIALS:
          serviceImpl.validateCredentials((com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest) request,
              (io.grpc.stub.StreamObserver<com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getLoginMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.socialseed.auth.AuthServiceOuterClass.LoginRequest,
              com.socialseed.auth.AuthServiceOuterClass.LoginResponse>(
                service, METHODID_LOGIN)))
        .addMethod(
          getRefreshMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.socialseed.auth.AuthServiceOuterClass.RefreshRequest,
              com.socialseed.auth.AuthServiceOuterClass.LoginResponse>(
                service, METHODID_REFRESH)))
        .addMethod(
          getIntrospectMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.socialseed.auth.AuthServiceOuterClass.IntrospectRequest,
              com.socialseed.auth.AuthServiceOuterClass.IntrospectResponse>(
                service, METHODID_INTROSPECT)))
        .addMethod(
          getValidateCredentialsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsRequest,
              com.socialseed.auth.AuthServiceOuterClass.ValidateCredentialsResponse>(
                service, METHODID_VALIDATE_CREDENTIALS)))
        .build();
  }

  private static abstract class AuthServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AuthServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.socialseed.auth.AuthServiceOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AuthService");
    }
  }

  private static final class AuthServiceFileDescriptorSupplier
      extends AuthServiceBaseDescriptorSupplier {
    AuthServiceFileDescriptorSupplier() {}
  }

  private static final class AuthServiceMethodDescriptorSupplier
      extends AuthServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AuthServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AuthServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AuthServiceFileDescriptorSupplier())
              .addMethod(getLoginMethod())
              .addMethod(getRefreshMethod())
              .addMethod(getIntrospectMethod())
              .addMethod(getValidateCredentialsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
