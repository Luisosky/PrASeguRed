package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Comentario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends MongoRepository<Comentario, String> {
    List<Comentario> findAllByIdReporte(String idReporte);
    List<Comentario> findAllByIdUsuario(String idUsuario);
}