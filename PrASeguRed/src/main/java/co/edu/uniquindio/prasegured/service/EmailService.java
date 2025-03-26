package co.edu.uniquindio.prasegured.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendSubscriptionConfirmation(String email) {
        String subject = "Confirmación de suscripción";
        String text = "Gracias por suscribirte a nuestro boletín. " +
                "Recibirás notificaciones de nuevos productos y ofertas especiales.";
        sendSimpleEmail(email, subject, text);
    }
}