package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.security.RecaptchaService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Profile("!test")
@RestController
@RequestMapping("/api/recaptcha")
@CrossOrigin(origins = "https://segured.vercel.app/") // Ajusta el origen de tu frontend
public class RecaptchaController {

    private final RecaptchaService recaptchaService;

    public RecaptchaController(RecaptchaService recaptchaService) {
        this.recaptchaService = recaptchaService;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyRecaptcha(@RequestBody Map<String, String> request) {
        String token = request.get("token");

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Token requerido"));
        }

        boolean isValid = recaptchaService.verifyRecaptcha(token);

        if (isValid) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Captcha válido"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Captcha inválido"));
        }
    }
}

