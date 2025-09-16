package net.ada.mailpit.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Activation de votre compte");
        message.setText("Bonjour,\n\nCliquez sur ce lien pour activer votre compte : " +
                "http://localhost:8070/api/auth/activate?code=" + code +
                "\n\nLe lien expire dans 24h.");
        mailSender.send(message);
    }
}
