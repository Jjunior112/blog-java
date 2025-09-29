    package com.blog_java.infra.sendGrid;

import com.blog_java.domain.ports.EmailSender;
import com.blog_java.infra.mailCatcher.SmtpEmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

    @Configuration
public class EmailConfig {

    //@Value("${api.security.sendGridApiKey}")
    //private String sendGridApiKey;
    //@Value("${api.security.from}")
    //private String from;

    //@Bean
    //@Profile("prod")
    //public EmailSender emailSender(){
    //    String apiKey = sendGridApiKey;
    //    return new SendGridEmailSender(apiKey,from);
    //}
    @Bean
    //@Profile("dev")
    public EmailSender devEmailSender() {

        return new SmtpEmailSender("localhost", 1025);
    }

}
