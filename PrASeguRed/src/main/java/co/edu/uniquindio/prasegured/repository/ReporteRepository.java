package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Reporte;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ReporteRepository extends MongoRepository<Reporte, String> {
    Optional<Reporte> findById(String id);
    Optional<Reporte> findByUsuarioId(String usuarioId);
}
