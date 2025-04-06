package co.edu.uniquindio.prasegured.dto;

public record ImagenRequest(
        String nombre,
        String reporteId,
        String usuarioId,
        byte[] content
) {
}
