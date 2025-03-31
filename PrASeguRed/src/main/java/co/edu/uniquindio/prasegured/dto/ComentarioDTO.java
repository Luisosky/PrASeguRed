package co.edu.uniquindio.prasegured.dto;

import java.util.Date;

public record ComentarioDTO(
        String id,
        String id_Reporte,
        String id_usuario,
        Boolean anonimo,
        String nombre,
        Date fechaPublicacion,
        String descripcion,
        float calificacion,
        int numeroCalificaciones) {
}
