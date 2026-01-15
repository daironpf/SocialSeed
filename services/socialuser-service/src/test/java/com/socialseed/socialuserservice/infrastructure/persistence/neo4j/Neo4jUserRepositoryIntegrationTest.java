package com.socialseed.socialuserservice.infrastructure.persistence.neo4j;

import com.socialseed.socialuserservice.testconfig.Neo4jIntegrationTest;
import com.socialseed.socialuserservice.user.domain.model.User;
import com.socialseed.socialuserservice.user.domain.model.UserLanguage;
import com.socialseed.socialuserservice.user.domain.model.UserStatus;
import com.socialseed.socialuserservice.user.domain.repository.UserRepository;
import com.socialseed.socialuserservice.user.infrastructure.persistence.neo4j.Neo4jSocialUserRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
@Import(Neo4jSocialUserRepositoryAdapter.class)
class Neo4jUserRepositoryIntegrationTest extends Neo4jIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void should_update_user_profile_in_neo4j() {
        // given
        UUID userId = UUID.randomUUID();

        User user = User.rehydrate(
                userId,
                "john_doe",
                "john@mail.com",
                "John",
                null,
                UserLanguage.EN,
                null,
                null,
                UserStatus.ACTIVE,
                null
        );

        userRepository.save(user);

        // when
        user.updateProfile(
                "John Updated",
                "Updated bio",
                "https://image.com/profile.png",
                LocalDate.of(1990, 5, 20),
                UserLanguage.ES
        );

        userRepository.updateProfile(user);

        // then
        User updated = userRepository.findById(userId).orElseThrow();

        assertThat(updated.getFullName()).isEqualTo("John Updated");
        assertThat(updated.getBio()).isEqualTo("Updated bio");
        assertThat(updated.getProfileImage()).isEqualTo("https://image.com/profile.png");
        assertThat(updated.getBirthDate()).isEqualTo(LocalDate.of(1990, 5, 20));
        assertThat(updated.getLanguage()).isEqualTo(UserLanguage.ES);
    }
}
