package co.edu.uniquindio.prasegured.dto;

import java.time.LocalDateTime;

public record ComentarioDTO(
        String usuarioId,
        String reporteId,
        String contenido,
        LocalDateTime fechaCreacion
) {}