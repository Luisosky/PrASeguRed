package co.edu.uniquindio.prasegured.dto;

import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;

import java.util.Date;
import java.util.List;

public record ComentarioDTO(
        String id,
        String idReporte,
        String idUsuario,
        String nombre,
        Date fechaPublicacion,
        String descripcion,
        int likes,
        int dislikes,
        ESTADOREPORTE estado,
        List<String> usersLiked,
        List<String> usersDisliked,
        Boolean anonimo,
        String userImage
) {}