package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    @Value("${spring.mail.username}")
    private String from;

    private final MailSender mailSender;
    @Autowired
    public EmailSenderService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String studentEmailAddress, String message, String subject) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(this.from);
        simpleMailMessage.setTo(studentEmailAddress);
        simpleMailMessage.setText(message);
        simpleMailMessage.setSubject(subject);
        mailSender.send(simpleMailMessage);

    }
}
