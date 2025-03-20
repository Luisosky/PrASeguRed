package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.EmailRequest;
import co.edu.uniquindio.prasegured.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/contact")
    public ResponseEntity<?> sendContactEmail(@Valid @RequestBody EmailRequest request) {
        try {
            emailService.sendSimpleMessage(
                    "support@prasegured.com",  // Your support email
                    "Contact Message: " + request.subject(),
                    "From: " + request.name() + " <" + request.email() + ">\n\n" + request.message()
            );

            Map<String, String> response = new HashMap<>();
            response.put("message", "Mensaje enviado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Error al enviar el mensaje");
            return ResponseEntity.badRequest().body(response);
        }
    }
}