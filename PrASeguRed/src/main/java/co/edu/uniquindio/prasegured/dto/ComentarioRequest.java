package co.edu.uniquindio.prasegured.dto;

public record ComentarioRequest(
        String idReporte,
        String idUsuario,
        Boolean anonimo,
        String nombre,
        String descripcion,
        int likes,
        int dislikes
) {}

