package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;

import java.util.List;

public interface ComentarioService {

    ComentarioDTO guardarComentario(ComentarioRequest request);

    ComentarioDTO obtenerComentario(String comentarioId);

    List<ComentarioDTO> obtenerComentariosPorReporte(String idReporte);

    List<ComentarioDTO> obtenerComentariosPorUsuario(String idUsuario);

    void eliminarComentario(String id);

    void darLike(String comentarioId, String userId);

    void quitarLike(String comentarioId, String userId);

    void darDislike(String comentarioId, String userId);

    void quitarDislike(String comentarioId, String userId);
}