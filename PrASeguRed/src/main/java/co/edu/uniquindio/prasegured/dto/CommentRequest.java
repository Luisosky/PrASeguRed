package co.edu.uniquindio.prasegured.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "El ID del usuario es requerido")
        String usuarioId,

        @NotBlank(message = "El ID del reporte es requerido")
        String reporteId,

        @NotBlank(message = "El contenido es requerido")
        @Size(max = 500, message = "El contenido no debe exceder los 500 caracteres")
        String contenido
) {}