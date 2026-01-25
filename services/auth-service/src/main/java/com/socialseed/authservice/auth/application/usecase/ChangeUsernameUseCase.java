package com.socialseed.authservice.auth.application.usecase;

import com.socialseed.authservice.auth.domain.repository.AuthUserRepository;
import com.socialseed.authservice.auth.domain.service.AuthService;
import com.socialseed.contracts.auth.events.AuthUserUsernameChanged;
import com.socialseed.contracts.socialuser.SocialUserProto;
import com.socialseed.contracts.socialuser.SocialUserServiceGrpc;
import com.socialseed.contracts.socialuser.UpdateUsernameRequest;
import com.socialseed.contracts.socialuser.UpdateUsernameResponse;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class ChangeUsernameUseCase {

  private static final Logger logger = LoggerFactory.getLogger(ChangeUsernameUseCase.class);

  private final AuthUserRepository authUserRepository;
  private final AuthService authService;
  private final KafkaTemplate<String, byte[]> kafkaTemplate;
  private final SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserServiceStub;

  public ChangeUsernameUseCase(AuthUserRepository authUserRepository,
      AuthService authService,
      KafkaTemplate<String, byte[]> kafkaTemplate,
      SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserServiceStub) {
    this.authUserRepository = authUserRepository;
    this.authService = authService;
    this.kafkaTemplate = kafkaTemplate;
    this.socialUserServiceStub = socialUserServiceStub;
  }

  @Transactional
  public void execute(UUID userId, String newUsername) {
    // 1. Validar que el usuario existe
    var user = authUserRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (user.getUsername().equals(newUsername)) {
      return; // No change
    }

    // 2. Validar unicidad
    if (authUserRepository.existByUsername(newUsername)) {
      throw new IllegalArgumentException("Username already exists");
    }

    String oldUsername = user.getUsername();

    // 3. Actualizar DB local
    authUserRepository.updateUsername(userId, newUsername);
    logger.info("Username updated in Auth DB for user {}", userId);

    // 4. Sync con SocialUser Service (gRPC)
    try {
      UpdateUsernameRequest request = UpdateUsernameRequest.newBuilder()
          .setUserId(userId.toString())
          .setNewUsername(newUsername)
          .build();

      UpdateUsernameResponse response = socialUserServiceStub.updateUsername(request);
      if (!response.getSuccess()) {
        logger.error("SocialUser service failed to update username: {}", response.getMessage());
        // Podríamos lanzar excepción para rollbackear transacción si queremos
        // consistencia fuerte
        // throw new RuntimeException("Failed to sync username with social service");
      }
    } catch (StatusRuntimeException e) {
      logger.error("gRPC call to SocialUser service failed", e);
      // Decisión: Lanzar excep para rollbackear porque es un cambio crítico de
      // identidad
      throw new RuntimeException("Failed to sync username with social service: " + e.getMessage());
    }

    // 5. Emitir evento Kafka
    AuthUserUsernameChanged event = AuthUserUsernameChanged.newBuilder()
        .setUserId(userId.toString())
        .setOldUsername(oldUsername)
        .setNewUsername(newUsername)
        .setUpdatedAt(com.google.protobuf.Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build())
        .build();

    kafkaTemplate.send("auth.user.username.changed", userId.toString(), event.toByteArray());
    logger.info("UsernameChanged event emitted for user {}", userId);
  }
}
