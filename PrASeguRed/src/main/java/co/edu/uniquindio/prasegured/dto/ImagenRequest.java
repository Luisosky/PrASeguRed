package co.edu.uniquindio.prasegured.dto;

public record ImagenRequest(
        String name,
        String reporteId,
        String usuarioId,
        byte[] content
) {
}
