package co.edu.uniquindio.prasegured.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    private final Map<String, String> verificationCodes = new HashMap<>();

    @Autowired
    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Genera un numero aleatorio de 6 digitos
     */
    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * Almaena un codigo de verificacion para un usuario
     */
    public void storeVerificationCode(String email, String code) {
        verificationCodes.put(email, code);
    }

    /**
     * Verifica el codigo de verificacion que le llego al usuario
     */
    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(code);
    }

    /**
     * remueve el codigo de verificacion del usuario (se tiene que usar despues de completar el registro)
     */
    public void clearVerificationCode(String email) {
        verificationCodes.remove(email);
    }

    /**
     * Envia un correo de verificacion al usuario
     */
    @Async
    public void sendVerificationEmail(String email, String name, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("PrASeguRed - Verificación de Registro");

            Context context = new Context(Locale.getDefault());
            context.setVariable("name", name);
            context.setVariable("verificationCode", code);

            String htmlContent = templateEngine.process("verification-email", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Verification email sent to: {}", email);

        } catch (MessagingException e) {
            logger.error("Failed to send verification email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Error sending verification email", e);
        }
    }

    /**
     * Enviar un correo de notificacion al administrador cuando se crea un nuevo reporte
     */
    @Async
    public void sendReportNotificationToAdmin(String adminEmail, String adminName, String reportTitle, String reportId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(adminEmail);
            helper.setSubject("PrASeguRed - Nuevo Reporte Creado");

            Context context = new Context(Locale.getDefault());
            context.setVariable("name", adminName);
            context.setVariable("reportTitle", reportTitle);
            context.setVariable("reportId", reportId);

            String htmlContent = templateEngine.process("report-notification", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Report notification email sent to admin: {}", adminEmail);

        } catch (MessagingException e) {
            logger.error("Failed to send report notification to admin {}: {}", adminEmail, e.getMessage());
            throw new RuntimeException("Error sending report notification email", e);
        }
    }

    /**
     * Envia una notificacion por correo electronico (tanto para admin para usuario, se envian las notificaciones generales)
     */
    @Async
    public void sendNotificationEmail(String email, String name, String title, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("PrASeguRed - " + title);

            Context context = new Context(Locale.getDefault());
            context.setVariable("name", name);
            context.setVariable("title", title);
            context.setVariable("content", content);

            String htmlContent = templateEngine.process("notification", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Notification email sent to: {}", email);

        } catch (MessagingException e) {
            logger.error("Failed to send notification email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Error sending notification email", e);
        }
    }

    /**
     * Envia un correo de restablecimiento de contraseña al usuario
     */
    @Async
    public void sendPasswordResetEmail(String email, String name, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("PrASeguRed - Restablecimiento de Contraseña");

            Context context = new Context(Locale.getDefault());
            context.setVariable("name", name);
            context.setVariable("resetToken", resetToken);

            String htmlContent = templateEngine.process("password-reset", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            logger.info("Password reset email sent to: {}", email);

        } catch (MessagingException e) {
            logger.error("Failed to send password reset email to {}: {}", email, e.getMessage());
            throw new RuntimeException("Error sending password reset email", e);
        }
    }
}