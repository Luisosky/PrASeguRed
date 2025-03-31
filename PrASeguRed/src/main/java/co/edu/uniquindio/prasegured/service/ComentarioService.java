package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    public Comentario registrarComentario(Comentario comentario) {
        // Aquí puedes agregar la lógica para registrar un comentario
        // Por ejemplo, guardar el comentario en la base de datos
        return comentarioRepository.save(comentario);
    }

    public List<Comentario> getAllComentarios () {
        // Aquí puedes agregar la lógica para obtener los comentarios de un reporte
        // Por ejemplo, consultar la base de datos y devolver la lista de comentarios
        return comentarioRepository.findAll();
    }

}
