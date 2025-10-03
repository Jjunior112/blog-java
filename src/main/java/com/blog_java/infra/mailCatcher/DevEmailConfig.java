    package com.blog_java.infra.mailCatcher;

import com.blog_java.domain.ports.EmailSender;
import com.blog_java.infra.sendGrid.SendGridEmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

    @Configuration
    @Profile("dev")
    public class DevEmailConfig {
        @Value("${spring.mail.host}")
        private String mailHost;

        @Value("${spring.mail.port}")
        private Integer mailPort;

        @Bean
        public EmailSender devEmailSender() {
            return new SmtpEmailSender(mailHost, mailPort);
        }

    }
