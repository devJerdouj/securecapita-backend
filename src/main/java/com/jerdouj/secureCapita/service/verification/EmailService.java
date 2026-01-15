package com.jerdouj.secureCapita.service.verification;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Verification Code");
        message.setText("Your verification code is:" + code + "\nThis code will expire in 24 hours.");
       javaMailSender.send(message);
    }
}
