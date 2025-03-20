package co.edu.uniquindio.prasegured.service.impl;

import co.edu.uniquindio.prasegured.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Service
public class EmailServiceImpl implements EmailService {

    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);


        try {
            mailSender.send(message);
            logger.info("Email enviado a: {}", to);
        } catch (Exception e) {
            logger.error("No se pudo enviar el email a  {}: {}", to, e.getMessage());
        }
    }

    @Async
    @Override
    public void sendHtmlMessage(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("HTML email enviado a: {}", to);
        } catch (MessagingException e) {
            logger.error("No se pudo enviar el HTML email a {}: {}", to, e.getMessage());
        }
    }

    @Async
    @Override
    public void sendSubscriptionConfirmation(String email) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("PrASeguRed - Confirmación de Suscripción");

            Context context = new Context(Locale.getDefault());
            context.setVariable("email", email);

            String htmlContent = templateEngine.process("subscription-confirmation", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Confirmación de Email enviada a: {}", email);
        } catch (MessagingException e) {
            logger.error("Falla en enviar el email de confirmación {}: {}", email, e.getMessage());
        }
    }
}