package org.example.watchfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("WatchFinder - Restablece tu contraseña");
        message.setText("Para resetear tu contraseña, haz click en el link de debajo:\n" + resetUrl);
        mailSender.send(message);
    }
}
