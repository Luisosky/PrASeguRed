package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/suscripcion")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/noticias")
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody Map<String, String> request) {
        String correo = request.get("correo"); // Asegurar que coincida con OpenAPI

        if (correo == null || correo.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El correo es requerido"));
        }

        try {
            emailService.sendSubscriptionConfirmation(correo);

            return ResponseEntity.ok(Map.of("message", "Suscripción realizada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error al enviar el correo"));
        }
    }
}
