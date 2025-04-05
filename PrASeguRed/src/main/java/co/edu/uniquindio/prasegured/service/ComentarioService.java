package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ComentarioService {
    ComentarioDTO guardarComentario(ComentarioRequest request);
    List<ComentarioDTO> obtenerComentariosPorReporte(String idReporte);
    List<ComentarioDTO> obtenerComentariosPorUsuario(String idUsuario);
    void eliminarComentario(String id);
    void denegarComentario(String id);
}
