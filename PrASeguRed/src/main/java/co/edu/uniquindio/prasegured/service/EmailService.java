package co.edu.uniquindio.prasegured.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendSubscriptionConfirmation(String email) throws MessagingException {
        // Crear contexto para Thymeleaf
        Context context = new Context();
        context.setVariable("email", email);

        // Verificar dónde está buscando la plantilla
        String templateLocation = "confirmationsub";

        // Procesar la plantilla Thymeleaf
        String htmlContent = templateEngine.process(templateLocation, context);

        // Enviar el correo
        sendHtmlEmail(email, "Confirmación de suscripción", htmlContent);
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
