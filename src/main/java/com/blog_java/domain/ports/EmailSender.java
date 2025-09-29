package com.blog_java.domain.ports;

public interface EmailSender {
    void sendEmail(String to, String subject, String body);
}
