package co.edu.uniquindio.prasegured.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private TemplateEngine templateEngine;

    public void sendVerificationCode(String email, String code) throws MessagingException {
        // Crear contexto para Thymeleaf
        Context context = new Context();
        context.setVariable("code", code);

        // Verificar dónde está buscando la plantilla
        String templateLocation = "verificationcode";

        // Procesar la plantilla Thymeleaf
        String htmlContent = templateEngine.process(templateLocation, context);

        // Enviar el correo
        sendEmail(email, "Código de Verificación", htmlContent);
    }

    public void sendSubscriptionConfirmation(String email) throws MessagingException {
        // Crear contexto para Thymeleaf
        Context context = new Context();
        context.setVariable("email", email);

        // Verificar dónde está buscando la plantilla
        String templateLocation = "confirmationsub";

        // Procesar la plantilla Thymeleaf
        String htmlContent = templateEngine.process(templateLocation, context);

        // Enviar el correo
        sendEmail(email, "Confirmación de suscripción", htmlContent);
    }

    public void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }


    public void enviarNotificacionPorEmail(String destinatario, String asunto, String contenido) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            
            // Crear contenido HTML para un mejor formato
            String htmlContent = "<html><body>" +
                                "<h2 style='color: #3366cc;'>" + asunto + "</h2>" +
                                "<div style='margin: 20px 0; padding: 20px; border-left: 4px solid #3366cc; background-color: #f9f9f9;'>" +
                                "<p>" + contenido + "</p>" +
                                "</div>" +
                                "<p style='font-size: 12px; color: #666;'>Este es un mensaje automático. Por favor no responda a este correo.</p>" +
                                "<hr>" +
                                "<p style='font-size: 11px;'>PrASeguRed - Sistema de Reportes Ciudadanos</p>" +
                                "</body></html>";
            
            helper.setText(htmlContent, true); // true indica que es contenido HTML
            
            mailSender.send(message);
            logger.info("Email de notificación enviado a: {}", destinatario);
        } catch (MessagingException e) {
            logger.error("Error al enviar email de notificación: {}", e.getMessage());
            throw e;
        }
    }
}