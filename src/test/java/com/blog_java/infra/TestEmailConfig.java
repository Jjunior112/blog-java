package com.blog_java.infra;

import com.blog_java.domain.ports.EmailSender;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestEmailConfig {
    @Bean
    public EmailSender emailSender() {
        return (to, subject, body) -> {
            System.out.println("Mock send email to " + to);
        };
    }
}
