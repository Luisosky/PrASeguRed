package co.edu.uniquindio.prasegured.dto;

import java.util.Date;
 

public record NotificacionDTO(
    String id,
    String usuarioId,
    String titulo,
    String categoria,
    String contenido,
    boolean leido,
    Date fechaCreacion,
    String reporteId

) {}