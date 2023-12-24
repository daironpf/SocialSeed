package com.social.seed.repository;

import com.social.seed.model.SocialUser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataNeo4jTest
class NewSocialUserRepositoryTest {
    @Autowired
    private SocialUserRepository underTest;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        underTest.deleteAll();
    }



    @Test
    void itShouldCheckWhenSocialUserByEmailExists() {
        // given
        // user #1
        String email = "maria1@gmail.com";
        underTest.save(
                SocialUser.builder()
                        .userName("maria1")
                        .email(email)
                        .dateBorn(LocalDateTime.parse("1992-01-04T00:00:00"))
                        .fullName("Maria del Laurel Perez")
                        .language("ES")
                        .registrationDate(LocalDateTime.now())
                        .isActive(true)
                        .isDeleted(false)
                        .onVacation(false)
                        .followersCount(0)
                        .friendCount(0)
                        .followingCount(0)
                        .friendRequestCount(0)
                        .build()
        );

        // when
        boolean expected = underTest.existByEmail(email);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void itShouldCheckWhenSocialUserByEmailDoesNotExists() {
        // given
        String email = "maria1@gmail.com";

        // when
        boolean expected = underTest.existByEmail(email);

        // then
        assertThat(expected).isFalse();
    }
}