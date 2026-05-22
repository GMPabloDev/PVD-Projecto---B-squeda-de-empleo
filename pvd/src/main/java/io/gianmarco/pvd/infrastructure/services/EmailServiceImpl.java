package io.gianmarco.pvd.infrastructure.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Year;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import io.gianmarco.pvd.application.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public void sendEmailVerification(String email, String name, String otp) {
        String htmlBody = buildEmailTemplate("email-verification", name, otp);
        send(email, "Confirm your email", htmlBody);
    }

    @Override
    public void sendForgotPassword(String email, String name, String otp) {
        String htmlBody = buildEmailTemplate("forgot-password", name, otp);
        send(email, "Reset your password", htmlBody);
    }

    private void send(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    "UTF-8");
            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
        }
    }

    private String buildEmailTemplate(String templateName, String name, String otp) {
        String path = "templates/" + templateName + ".html";
        try {
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream(path);

            if (inputStream == null) {
                throw new RuntimeException("Template not found: " + templateName);
            }

            String template = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            return template
                    .replace("{{USERNAME}}", name)
                    .replace("{{CODE}}", otp)
                    .replace("{{EXPIRATION_MINUTES}}", String.valueOf(15))
                    .replace("{{CURRENT_YEAR}}", String.valueOf(Year.now().getValue()));

        } catch (IOException e) {
            throw new RuntimeException("Failed to load template: " + templateName, e);
        }
    }
}
