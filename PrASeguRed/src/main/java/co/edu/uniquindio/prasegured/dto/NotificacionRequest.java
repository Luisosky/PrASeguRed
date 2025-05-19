package co.edu.uniquindio.prasegured.dto;



public record NotificacionRequest(
    String usuarioId,
    String titulo,
    String categoria,
    String contenido,
    String reporteId

) {}