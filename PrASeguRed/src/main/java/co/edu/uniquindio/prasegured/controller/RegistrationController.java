package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.VerificationRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Registration", description = "API for user registration and verification")
public class RegistrationController {

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Object userRequest) {

        return new ResponseEntity<>(
                Map.of("message", "Registro exitoso y envío de código de verificación."),
                HttpStatus.CREATED);
    }

    @PostMapping("/email")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestBody VerificationRequest request) {

        return ResponseEntity.ok(Map.of("message", "Código enviado correctamente."));
    }
}