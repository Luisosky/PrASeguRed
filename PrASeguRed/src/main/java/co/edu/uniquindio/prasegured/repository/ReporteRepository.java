package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Reporte;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteRepository extends MongoRepository<Reporte, String> {
    Optional<Reporte> findById(String id);
    List<Reporte> findByIdUsuario(String idUsuario);
}
