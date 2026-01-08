package com.socialseed.socialuserservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // desactiva filtros de seguridad
class SocialuserServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
