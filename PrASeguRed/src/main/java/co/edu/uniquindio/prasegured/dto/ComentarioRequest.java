package co.edu.uniquindio.prasegured.dto;

public record ComentarioRequest(
        String id_Reporte,
        String id_usuario,
        Boolean anonimo,
        String nombre,
        String descripcion,
        float calificacion
) {}

