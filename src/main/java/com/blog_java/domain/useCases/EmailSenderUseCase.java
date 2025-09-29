package com.blog_java.domain.useCases;


public interface EmailSenderUseCase {
    void sendEmail(String to, String subject,String body);
}