package com.socialseed.socialuserservice.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Servicio gRPC que expone SocialUserService
 * </pre>
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class SocialUserServiceGrpc {

  private SocialUserServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "SocialUserService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.socialseed.socialuserservice.proto.CreateUserRequest,
      com.socialseed.socialuserservice.proto.CreateUserReply> getCreateUserMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateUser",
      requestType = com.socialseed.socialuserservice.proto.CreateUserRequest.class,
      responseType = com.socialseed.socialuserservice.proto.CreateUserReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.socialseed.socialuserservice.proto.CreateUserRequest,
      com.socialseed.socialuserservice.proto.CreateUserReply> getCreateUserMethod() {
    io.grpc.MethodDescriptor<com.socialseed.socialuserservice.proto.CreateUserRequest, com.socialseed.socialuserservice.proto.CreateUserReply> getCreateUserMethod;
    if ((getCreateUserMethod = SocialUserServiceGrpc.getCreateUserMethod) == null) {
      synchronized (SocialUserServiceGrpc.class) {
        if ((getCreateUserMethod = SocialUserServiceGrpc.getCreateUserMethod) == null) {
          SocialUserServiceGrpc.getCreateUserMethod = getCreateUserMethod =
              io.grpc.MethodDescriptor.<com.socialseed.socialuserservice.proto.CreateUserRequest, com.socialseed.socialuserservice.proto.CreateUserReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateUser"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.socialseed.socialuserservice.proto.CreateUserRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.socialseed.socialuserservice.proto.CreateUserReply.getDefaultInstance()))
              .setSchemaDescriptor(new SocialUserServiceMethodDescriptorSupplier("CreateUser"))
              .build();
        }
      }
    }
    return getCreateUserMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SocialUserServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SocialUserServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SocialUserServiceStub>() {
        @java.lang.Override
        public SocialUserServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SocialUserServiceStub(channel, callOptions);
        }
      };
    return SocialUserServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static SocialUserServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SocialUserServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SocialUserServiceBlockingV2Stub>() {
        @java.lang.Override
        public SocialUserServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SocialUserServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return SocialUserServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SocialUserServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SocialUserServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SocialUserServiceBlockingStub>() {
        @java.lang.Override
        public SocialUserServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SocialUserServiceBlockingStub(channel, callOptions);
        }
      };
    return SocialUserServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SocialUserServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SocialUserServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SocialUserServiceFutureStub>() {
        @java.lang.Override
        public SocialUserServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SocialUserServiceFutureStub(channel, callOptions);
        }
      };
    return SocialUserServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Servicio gRPC que expone SocialUserService
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Crea un usuario con username y email
     * </pre>
     */
    default void createUser(com.socialseed.socialuserservice.proto.CreateUserRequest request,
        io.grpc.stub.StreamObserver<com.socialseed.socialuserservice.proto.CreateUserReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateUserMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SocialUserService.
   * <pre>
   * Servicio gRPC que expone SocialUserService
   * </pre>
   */
  public static abstract class SocialUserServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SocialUserServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SocialUserService.
   * <pre>
   * Servicio gRPC que expone SocialUserService
   * </pre>
   */
  public static final class SocialUserServiceStub
      extends io.grpc.stub.AbstractAsyncStub<SocialUserServiceStub> {
    private SocialUserServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SocialUserServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SocialUserServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Crea un usuario con username y email
     * </pre>
     */
    public void createUser(com.socialseed.socialuserservice.proto.CreateUserRequest request,
        io.grpc.stub.StreamObserver<com.socialseed.socialuserservice.proto.CreateUserReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SocialUserService.
   * <pre>
   * Servicio gRPC que expone SocialUserService
   * </pre>
   */
  public static final class SocialUserServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<SocialUserServiceBlockingV2Stub> {
    private SocialUserServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SocialUserServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SocialUserServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * Crea un usuario con username y email
     * </pre>
     */
    public com.socialseed.socialuserservice.proto.CreateUserReply createUser(com.socialseed.socialuserservice.proto.CreateUserRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getCreateUserMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service SocialUserService.
   * <pre>
   * Servicio gRPC que expone SocialUserService
   * </pre>
   */
  public static final class SocialUserServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SocialUserServiceBlockingStub> {
    private SocialUserServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SocialUserServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SocialUserServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Crea un usuario con username y email
     * </pre>
     */
    public com.socialseed.socialuserservice.proto.CreateUserReply createUser(com.socialseed.socialuserservice.proto.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateUserMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SocialUserService.
   * <pre>
   * Servicio gRPC que expone SocialUserService
   * </pre>
   */
  public static final class SocialUserServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<SocialUserServiceFutureStub> {
    private SocialUserServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SocialUserServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SocialUserServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Crea un usuario con username y email
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.socialseed.socialuserservice.proto.CreateUserReply> createUser(
        com.socialseed.socialuserservice.proto.CreateUserRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateUserMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_USER = 0;

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
        case METHODID_CREATE_USER:
          serviceImpl.createUser((com.socialseed.socialuserservice.proto.CreateUserRequest) request,
              (io.grpc.stub.StreamObserver<com.socialseed.socialuserservice.proto.CreateUserReply>) responseObserver);
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
          getCreateUserMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.socialseed.socialuserservice.proto.CreateUserRequest,
              com.socialseed.socialuserservice.proto.CreateUserReply>(
                service, METHODID_CREATE_USER)))
        .build();
  }

  private static abstract class SocialUserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SocialUserServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.socialseed.socialuserservice.proto.SocialUserProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SocialUserService");
    }
  }

  private static final class SocialUserServiceFileDescriptorSupplier
      extends SocialUserServiceBaseDescriptorSupplier {
    SocialUserServiceFileDescriptorSupplier() {}
  }

  private static final class SocialUserServiceMethodDescriptorSupplier
      extends SocialUserServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SocialUserServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (SocialUserServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SocialUserServiceFileDescriptorSupplier())
              .addMethod(getCreateUserMethod())
              .build();
        }
      }
    }
    return result;
  }
}
