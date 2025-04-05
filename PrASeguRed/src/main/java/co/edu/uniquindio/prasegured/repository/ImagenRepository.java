package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Imagen;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImagenRepository extends MongoRepository<Imagen, String> {
    Optional<Imagen> findById(String id);
    Optional<Imagen> findByReporteId(String reporteId);
    Optional<Imagen> findByUsuarioId(String usuarioId);
}
