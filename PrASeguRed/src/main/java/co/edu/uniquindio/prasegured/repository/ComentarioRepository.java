package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ComentarioRepository extends MongoRepository<Comentario, String> {
    Comentario findById_Comentario(String id_Comentario);
}
