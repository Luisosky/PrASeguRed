package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.Imagen;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImagenRepository extends MongoRepository<Imagen, String> {
    // Nota: findById ya está incluido en MongoRepository,
    // así que técnicamente es redundante pero no causa problemas
    Optional<Imagen> findById(String id);

    // Queries existentes
    List<Imagen> findByReporteId(String reporteId);
    List<Imagen> findByUsuarioId(String usuarioId);

    // Nuevas queries útiles

    // Para eliminar todas las imágenes de un reporte
    void deleteByReporteId(String reporteId);

    // Buscar por estado (útil para limpieza o mantenimiento)
    List<Imagen> findByEstado(ESTADOREPORTE estado);

    // Buscar imágenes por reporte y estado
    List<Imagen> findByReporteIdAndEstado(String reporteId, ESTADOREPORTE estado);

    // Contar imágenes por reporte (útil para validaciones)
    long countByReporteId(String reporteId);

    // Buscar imágenes por nombre (útil para verificar duplicados)
    List<Imagen> findByNombreContaining(String nombreParcial);

    // Buscar por usuario y estado
    List<Imagen> findByUsuarioIdAndEstado(String usuarioId, ESTADOREPORTE estado);
}