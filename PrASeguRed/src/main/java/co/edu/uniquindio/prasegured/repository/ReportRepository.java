package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReportRepository extends MongoRepository<Report, String> {
    List<Report> findByUserId(String userId);
    List<Report> findByImportante(boolean importante);
    List<Report> findByResuelto(boolean resuelto);
}