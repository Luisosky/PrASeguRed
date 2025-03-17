package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.UserRegistrationRequest;
import co.edu.uniquindio.prasegured.model.User;
import co.edu.uniquindio.prasegured.service.EmailService;
import co.edu.uniquindio.prasegured.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Authentication", description = "API for user registration and authentication")
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Operation(
            summary = "Registra un nuevo usuario",
            description = "Registra un nuevo usuario en el sistema y retorna los datos del usuario creado",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User created successfully",
                            content = @Content(schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data"
                    )
            }
    )
    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UserRegistrationRequest request) {
        try {
            User savedUser = userService.registerUser(request);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


}