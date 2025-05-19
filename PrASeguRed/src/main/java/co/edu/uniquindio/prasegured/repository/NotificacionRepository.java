package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends MongoRepository<Notificacion, String> {
    
    // Buscar notificaciones por ID de usuario
    List<Notificacion> findByUsuarioId(String usuarioId);
    
    // Buscar notificaciones por ID de usuario con paginación
    Page<Notificacion> findByUsuarioId(String usuarioId, Pageable pageable);
    
    // Buscar notificaciones por ID de usuario y estado de lectura
    Page<Notificacion> findByUsuarioIdAndLeido(String usuarioId, boolean leido, Pageable pageable);
    
    // Buscar notificaciones por ID de reporte
    List<Notificacion> findByReporteId(String reporteId);
    
    // Contar notificaciones no leídas de un usuario
    long countByUsuarioIdAndLeido(String usuarioId, boolean leido);
}