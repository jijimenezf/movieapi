package dev.journey.movieapi.services;

import dev.journey.movieapi.MailBody;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender sender) {
        this.javaMailSender = sender;
    }

    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailBody.to());
        mailMessage.setFrom("sudarshaneinstein@gmail.com");
        mailMessage.setSubject(mailBody.subject());
        mailMessage.setText(mailBody.text());

        javaMailSender.send(mailMessage);
    }
}
