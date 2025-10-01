package com.blog_java.infra.sendGrid;

import com.blog_java.domain.ports.EmailSender;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class SendGridEmailSender implements EmailSender {
    private final SendGrid sendGrid;

    @Value("${api.security.from}")
    private final String from;

    public SendGridEmailSender(String apiKey, String from) {
        this.sendGrid = new SendGrid(apiKey);
        this.from = from;
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        Email fromEmail = new Email(from);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            if (response.getStatusCode() >= 400) {
                throw new RuntimeException("Erro ao enviar email: " + response.getBody());
            }

        } catch (IOException ex) {
            throw new RuntimeException("Falha na comunicação com SendGrid", ex);
        }
    }
}
