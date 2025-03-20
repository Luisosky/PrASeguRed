package co.edu.uniquindio.prasegured.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        @NotBlank(message = "El correo es requerido")
        @Email(message = "Debe ser un email válido")
        String correo,

        @NotBlank(message = "La contraseña es requerida")
        String contraseña
) {}