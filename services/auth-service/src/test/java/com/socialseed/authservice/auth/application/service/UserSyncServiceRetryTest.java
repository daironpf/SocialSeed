package com.socialseed.authservice.auth.application.service;

import com.socialseed.contracts.socialuser.SocialUserServiceGrpc;
import com.socialseed.contracts.socialuser.UpdateEmailRequest;
import com.socialseed.contracts.socialuser.UpdateEmailResponse;
import com.socialseed.contracts.socialuser.UpdateUsernameRequest;
import com.socialseed.contracts.socialuser.UpdateUsernameResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserSyncServiceRetryTest {

  @Autowired
  private UserSyncService userSyncService;

  @MockBean
  private SocialUserServiceGrpc.SocialUserServiceBlockingStub socialUserServiceStub;

  @MockBean
  private KafkaTemplate<String, byte[]> kafkaTemplate;

  @Test
  void shouldRetryOnGrpcFailureAndThenSucceed() {
    UUID userId = UUID.randomUUID();
    String oldVal = "old";
    String newVal = "new";

    UpdateUsernameResponse successResponse = UpdateUsernameResponse.newBuilder().setSuccess(true).build();

    // 1st and 2nd calls fail, 3rd call succeeds
    when(socialUserServiceStub.updateUsername(any(UpdateUsernameRequest.class)))
        .thenThrow(new StatusRuntimeException(Status.UNAVAILABLE))
        .thenThrow(new StatusRuntimeException(Status.UNAVAILABLE))
        .thenReturn(successResponse);

    userSyncService.syncUsernameChange(userId, oldVal, newVal);

    verify(socialUserServiceStub, times(3)).updateUsername(any(UpdateUsernameRequest.class));
    verify(kafkaTemplate, times(1)).send(eq("auth.user.username.changed"), eq(userId.toString()), any(byte[].class));
  }

  @Test
  void shouldRetryAndRecoverAfterMaxAttempts() {
    UUID userId = UUID.randomUUID();
    String oldEmail = "old@test.com";
    String newEmail = "new@test.com";

    // All calls fail
    when(socialUserServiceStub.updateEmail(any(UpdateEmailRequest.class)))
        .thenThrow(new StatusRuntimeException(Status.DEADLINE_EXCEEDED));

    userSyncService.syncEmailChange(userId, oldEmail, newEmail);

    // Verification: 3 attempts total (maxAttempts = 3)
    verify(socialUserServiceStub, times(3)).updateEmail(any(UpdateEmailRequest.class));

    // Verification: Failure pushed to inspection topic
    verify(kafkaTemplate, times(1)).send(eq("auth.user.sync.failures"), eq(userId.toString()), any(byte[].class));

    // Ensure NO success message sent to production topic
    verify(kafkaTemplate, never()).send(eq("auth.user.email.changed"), anyString(), any(byte[].class));
  }
}
