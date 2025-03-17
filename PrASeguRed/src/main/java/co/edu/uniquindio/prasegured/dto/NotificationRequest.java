package co.edu.uniquindio.prasegured.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NotificationRequest(
        @NotBlank(message = "El ID del usuario es requerido")
        String usuarioId,

        @NotBlank(message = "El título es requerido")
        @Size(max = 100, message = "El título no debe exceder los 100 caracteres")
        String titulo,

        @NotBlank(message = "La categoría es requerida")
        String categoria,

        @NotBlank(message = "El contenido es requerido")
        @Size(max = 500, message = "El contenido no debe exceder los 500 caracteres")
        String contenido
) {}