package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.SubscriptionRequest;
import co.edu.uniquindio.prasegured.exception.EmailExistException;
import co.edu.uniquindio.prasegured.service.SubscriptionService;
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
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<?> subscribe(@Valid @RequestBody SubscriptionRequest request) {
        try {
            subscriptionService.subscribe(request);

            Map<String, String> response = new HashMap<>();
            response.put("message", "¡Gracias por suscribirte a nuestro boletín!");
            return ResponseEntity.ok(response);
        } catch (EmailExistException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}