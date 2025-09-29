package com.blog_java.infra.mailCatcher;

import com.blog_java.domain.ports.EmailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class SmtpEmailSender implements EmailSender {

    private final JavaMailSenderImpl mailSender;

    public SmtpEmailSender(String host, int port) {
        this.mailSender = new JavaMailSenderImpl();
        this.mailSender.setHost(host);
        this.mailSender.setPort(port);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
