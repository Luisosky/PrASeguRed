package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ComentarioRepository extends MongoRepository<Comentario, String> {
    Comentario findByComentario(String comentario);
}
