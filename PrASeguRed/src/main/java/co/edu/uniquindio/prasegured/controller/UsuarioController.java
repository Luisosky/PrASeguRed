package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.CodigoVerificacionDTO;
import co.edu.uniquindio.prasegured.dto.UsuarioRegistroDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsuarioController {

    @Operation(
            summary = "Registro de usuario",
            description = "Registra un nuevo usuario en el sistema",
            operationId = "registrarUsuario"
    )
    @ApiResponse(responseCode = "201", description = "Registro exitoso, se enviará un código de verificación.")
    @PostMapping("/registro")
    public ResponseEntity<Void> registrarUsuario(@RequestBody UsuarioRegistroDTO usuario) {
        // Implementación del registro de usuario
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Enviar código de verificación",
            description = "Envía un correo con el código de verificación al usuario registrado",
            operationId = "enviarCodigoVerificacion"
    )
    @ApiResponse(responseCode = "200", description = "Código enviado correctamente.")
    @PostMapping("/email/enviar")
    public ResponseEntity<Void> enviarCodigoVerificacion(@RequestBody CodigoVerificacionDTO codigo) {
        // Implementación para enviar código de verificación
        return ResponseEntity.ok().build();
    }
}