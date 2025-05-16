package co.edu.uniquindio.prasegured.dto;

import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;

public record ImagenMetadataDTO(
        String id,
        String reporteId,
        String usuarioId,
        String nombre,
        long contentLength, // Tamaño del contenido en bytes
        String contentType, // Tipo de contenido (MIME)
        String imageUrl,    // URL para acceder a la imagen
        ESTADOREPORTE estado
) {}