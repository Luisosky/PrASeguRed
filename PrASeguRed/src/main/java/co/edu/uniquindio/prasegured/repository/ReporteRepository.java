package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Reporte;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReporteRepository extends MongoRepository<Reporte, String> {
    Reporte findByReporte(String reporte);
}
