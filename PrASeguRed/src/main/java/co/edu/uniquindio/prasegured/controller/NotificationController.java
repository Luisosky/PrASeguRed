package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.NotificationRequest;
import co.edu.uniquindio.prasegured.model.Notification;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Notifications", description = "API for notification management")
public class NotificationController {

    @PostMapping("/notificacion")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody NotificationRequest request) {

        return new ResponseEntity<>(
                Map.of("message", "Notificación enviada correctamente."),
                HttpStatus.CREATED);
    }

    @GetMapping("/notificaciones/usuario/{usuarioId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(@PathVariable String usuarioId) {

        List<Notification> notifications = new ArrayList<>();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/notificaciones")
    public ResponseEntity<List<Notification>> getAllNotifications() {

        List<Notification> notifications = new ArrayList<>();
        return ResponseEntity.ok(notifications);
    }
}