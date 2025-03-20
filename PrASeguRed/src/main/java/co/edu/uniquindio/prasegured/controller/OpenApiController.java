package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.*;
import co.edu.uniquindio.prasegured.service.AuthService;
import co.edu.uniquindio.prasegured.service.ReportService;
import co.edu.uniquindio.prasegured.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OpenApiController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        userService.registerUser(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario registrado correctamente. Verifique su correo electrónico.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/auth/verify")
    public ResponseEntity<Map<String, String>> verifyAccount(@Valid @RequestBody VerificationRequest request) {
        userService.verifyAccount(request.codigoVerificacion());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cuenta verificada correctamente.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            String token = authService.authenticateUser(request.getCorreo(), request.getContraseña());
            AuthResponse response = new AuthResponse(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(
                            HttpStatus.UNAUTHORIZED,
                            "Error de autenticación",
                            List.of("Credenciales incorrectas")
                    ));
        }
    }

    @PutMapping("/users/account")
    public ResponseEntity<Map<String, String>> updateAccount(
            @Valid @RequestBody AccountUpdateRequest request,
            @RequestHeader("Authorization") String token) {

        userService.updateUser(request, token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Cuenta actualizada correctamente.");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/account")
    public ResponseEntity<Map<String, String>> deleteAccount(
            @Valid @RequestBody DeleteAccountRequest request,
            @RequestHeader("Authorization") String token) {

        if (request.confirmacion()) {
            userService.deleteUserAccount(token);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Cuenta eliminada correctamente.");
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Debe confirmar la eliminación de la cuenta"));
        }
    }

    @PostMapping("/reports")
    public ResponseEntity<Map<String, String>> createReport(
            @Valid @RequestBody ReportRequest request,
            @RequestHeader("Authorization") String token) {

        String reportId = reportService.createReport(request, token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Reporte creado correctamente");
        response.put("reportId", reportId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/reports/comments")
    public ResponseEntity<Map<String, String>> addComment(
            @Valid @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String token) {

        reportService.addComment(request, token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Comentario agregado correctamente");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/notifications")
    public ResponseEntity<Map<String, String>> createNotification(
            @Valid @RequestBody NotificationRequest request,
            @RequestHeader("Authorization") String token) {

        userService.createNotification(request, token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Notificación creada correctamente");
        return ResponseEntity.ok(response);
    }
}