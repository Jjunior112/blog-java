package com.blog_java;

import com.blog_java.infra.TestEmailConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestEmailConfig.class)
class BlogJavaApplicationTests {

	@Test
	void contextLoads() {
	}

}
