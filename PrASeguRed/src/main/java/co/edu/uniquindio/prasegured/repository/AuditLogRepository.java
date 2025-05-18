package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, Long> {
}
