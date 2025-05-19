package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.mapper.ComentarioMapper;
import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ComentarioServiceImple implements ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ComentarioMapper comentarioMapper;

    @Override
    public ComentarioDTO guardarComentario(ComentarioRequest request) {
        Comentario comentario = comentarioMapper.toComentario(request);
        return comentarioMapper.toDTO(comentarioRepository.save(comentario));
    }

    @Override
    public ComentarioDTO obtenerComentario(String comentarioId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el comentario con ID: " + comentarioId));

        return comentarioMapper.toDTO(comentario);
    }

    @Override
    public List<ComentarioDTO> obtenerComentariosPorReporte(String idReporte) {
        List<Comentario> comentarios = comentarioRepository.findByIdReporte(idReporte);
        return comentarioMapper.toDTOList(comentarios);
    }

    @Override
    public List<ComentarioDTO> obtenerComentariosPorUsuario(String idUsuario) {
        List<Comentario> comentarios = comentarioRepository.findByIdUsuario(idUsuario);
        return comentarioMapper.toDTOList(comentarios);
    }

    @Override
    public void eliminarComentario(String id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el comentario con ID: " + id));

        comentario.setEstado(ESTADOREPORTE.Eliminado);
        comentarioRepository.save(comentario);
    }

    @Override
    public void darLike(String comentarioId, String userId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el comentario con ID: " + comentarioId));

        // Inicializar listas si son null
        if (comentario.getUsersLiked() == null) {
            comentario.setUsersLiked(new ArrayList<>());
        }
        if (comentario.getUsersDisliked() == null) {
            comentario.setUsersDisliked(new ArrayList<>());
        }

        // Quitar de dislikes si existe
        if (comentario.getUsersDisliked().contains(userId)) {
            comentario.getUsersDisliked().remove(userId);
            if (comentario.getDislikes() > 0) {
                comentario.setDislikes(comentario.getDislikes() - 1);
            }
        }

        // Añadir a likes si no existe
        if (!comentario.getUsersLiked().contains(userId)) {
            comentario.getUsersLiked().add(userId);
            comentario.setLikes(comentario.getLikes() + 1);
        }

        comentarioRepository.save(comentario);
    }

    @Override
    public void quitarLike(String comentarioId, String userId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el comentario con ID: " + comentarioId));

        // Inicializar lista si es null
        if (comentario.getUsersLiked() == null) {
            comentario.setUsersLiked(new ArrayList<>());
        } else if (comentario.getUsersLiked().contains(userId)) {
            comentario.getUsersLiked().remove(userId);
            if (comentario.getLikes() > 0) {
                comentario.setLikes(comentario.getLikes() - 1);
            }
        }

        comentarioRepository.save(comentario);
    }

    @Override
    public void darDislike(String comentarioId, String userId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el comentario con ID: " + comentarioId));

        // Inicializar listas si son null
        if (comentario.getUsersLiked() == null) {
            comentario.setUsersLiked(new ArrayList<>());
        }
        if (comentario.getUsersDisliked() == null) {
            comentario.setUsersDisliked(new ArrayList<>());
        }

        // Quitar de likes si existe
        if (comentario.getUsersLiked().contains(userId)) {
            comentario.getUsersLiked().remove(userId);
            if (comentario.getLikes() > 0) {
                comentario.setLikes(comentario.getLikes() - 1);
            }
        }

        // Añadir a dislikes si no existe
        if (!comentario.getUsersDisliked().contains(userId)) {
            comentario.getUsersDisliked().add(userId);
            comentario.setDislikes(comentario.getDislikes() + 1);
        }

        comentarioRepository.save(comentario);
    }

    @Override
    public void quitarDislike(String comentarioId, String userId) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new NoSuchElementException("No se encontró el comentario con ID: " + comentarioId));

        // Inicializar lista si es null
        if (comentario.getUsersDisliked() == null) {
            comentario.setUsersDisliked(new ArrayList<>());
        } else if (comentario.getUsersDisliked().contains(userId)) {
            comentario.getUsersDisliked().remove(userId);
            if (comentario.getDislikes() > 0) {
                comentario.setDislikes(comentario.getDislikes() - 1);
            }
        }

        comentarioRepository.save(comentario);
    }
}