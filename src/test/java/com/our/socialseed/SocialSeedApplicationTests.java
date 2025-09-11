package com.our.socialseed;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureMockMvc(addFilters = false) // desactiva filtros de seguridad
@SpringBootTest
class SocialSeedApplicationTests {

	@Test
	void contextLoads() {
	}

}
