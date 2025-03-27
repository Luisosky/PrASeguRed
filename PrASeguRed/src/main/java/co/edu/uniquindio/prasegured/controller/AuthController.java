package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.CredencialesDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Operation(
            summary = "Inicio de sesión",
            description = "Autentica a un usuario con correo y contraseña",
            operationId = "login"
    )
    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso.")
    @ApiResponse(responseCode = "401", description = "Credenciales incorrectas.")
    @PostMapping("/auth/login")
    public ResponseEntity<Void> login(@RequestBody CredencialesDTO credenciales) {
        // Implementación de la autenticación
        // Por ahora retornamos OK para probar que el endpoint responde
        return ResponseEntity.ok().build();
    }
}