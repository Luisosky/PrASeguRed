package co.edu.uniquindio.prasegured.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ReportRequest(
        @NotBlank(message = "El título es requerido")
        @Size(max = 100, message = "El título no debe exceder los 100 caracteres")
        String titulo,

        Integer limiteEdad,

        @Size(max = 1000, message = "La descripción no debe exceder los 1000 caracteres")
        String descripcion,

        String ubicacion,
        Boolean importante,
        Boolean resuelto,
        List<String> imagenes,

        @NotBlank(message = "La categoría es requerida")
        String categoria
) {}