package net.ada.mailpit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailTestRunner implements CommandLineRunner {

    private final JavaMailSender mailSender;

    public MailTestRunner(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void run(String... args) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("test@example.com"); // adresse de test
        message.setSubject("Test Mailpit");
        message.setText("Bonjour, ceci est un mail de test envoyé via Mailpit !");
        mailSender.send(message);

        System.out.println("Mail envoyé ! Vérifie l'interface http://localhost:8025");
    }
}
