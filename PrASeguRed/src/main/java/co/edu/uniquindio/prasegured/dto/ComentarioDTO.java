package co.edu.uniquindio.prasegured.dto;

import co.edu.uniquindio.prasegured.model.EnumEstado;

import java.util.Date;

public record ComentarioDTO(
        String id,
        String idReporte,
        String idUsuario,
        Boolean anonimo,
        String nombre,
        Date fechaPublicacion,
        String descripcion,
        int likes,
        int dislikes,
        EnumEstado estado
) {}
