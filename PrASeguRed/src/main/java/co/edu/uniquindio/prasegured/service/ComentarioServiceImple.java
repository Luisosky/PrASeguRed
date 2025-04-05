package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import co.edu.uniquindio.prasegured.mapper.ComentarioMapper;
import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImple implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ComentarioMapper comentarioMapper;

    @Override
    public ComentarioDTO guardarComentario(ComentarioRequest request) {
        Comentario comentario = comentarioMapper.toComentario(request);
        comentario = comentarioRepository.save(comentario);
        return comentarioMapper.toComentarioDTO(comentario);
    }

    @Override
    public List<ComentarioDTO> obtenerComentariosPorReporte(String idReporte) {
        return comentarioRepository.findAllById_Reporte(idReporte)
                .stream()
                .map(comentarioMapper::toComentarioDTO)
                .toList();
    }

    @Override
    public List<ComentarioDTO> obtenerComentariosPorUsuario(String idUsuario) {
        return comentarioRepository.findAllById_usuario(idUsuario)
                .stream()
                .map(comentarioMapper::toComentarioDTO)
                .toList();
    }

    @Override
    public void eliminarComentario(String id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new ValueConflictException("No existe un comentario con id: " + id));
        comentario.setEstado(EnumEstado.Eliminado);
        comentarioRepository.save(comentario);
    }

    @Override
    public void denegarComentario(String id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new ValueConflictException("No existe un comentario con id: " + id));
        comentario.setEstado(EnumEstado.Denegado);
        comentarioRepository.save(comentario);
    }
}
