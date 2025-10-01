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

        @Bean
        public EmailSender devEmailSender() {
            return new SmtpEmailSender("localhost", 1025);
        }

    }
