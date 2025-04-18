package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.exception.ResourceNotFoundException;
import co.edu.uniquindio.prasegured.mapper.ReporteMapper;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReporteServiceImple implements ReporteService {
    private final ReporteRepository reporteRepository;
    private final ReporteMapper reporteMapper;


    @Override
    public ReporteDTO save(ReporteRequest reporte) {
        var newReporte = reporteMapper.parseOf(reporte);
        validateReporteid(reporte.id());
        return reporteMapper.toReporteDTO(
                reporteRepository.save(newReporte)
        );
    }

    @Override
    public ReporteDTO update(String id, ReporteRequest reporte) {
        // Buscar el reporte existente en la base de datos
        var existingReporte = reporteRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        if (!existingReporte.getTitulo().equals(reporte.titulo())) {
            validateReporteid(reporte.id());
        }
        existingReporte.setTitulo(reporte.titulo());
        existingReporte.setDescripcion(reporte.descripcion());
        existingReporte.setUbicacion(reporte.ubicacion());
        existingReporte.setCategoria(reporte.categoria());
        existingReporte.setLocations(reporte.locations());
        existingReporte.setFechaActualizacion(new Date()); // Actualizar la fecha de modificación
        var savedReporte = reporteRepository.save(existingReporte);
        return reporteMapper.toReporteDTO(savedReporte);
    }

    @Override
    public List<ReporteDTO> findAll() {
        return reporteRepository.findAll()
                .stream()
                .map(reporteMapper::toReporteDTO)
                .toList();
    }

    @Override
    public ReporteDTO findById(String id) {
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return reporteMapper.toReporteDTO(storedReporte); // Mapear Reporte a ReporteDTO
    }

    @Override
    public void deleteById(String id) {
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        storedReporte.setEstado(EnumEstado.Eliminado);
        storedReporte.setFechaActualizacion(new Date());
        reporteRepository.save(storedReporte);
    }

    @Override
    public void reporteCompleto(String id) {
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        storedReporte.setEstado(EnumEstado.Completado);
        storedReporte.setFechaActualizacion(new Date());
        reporteRepository.save(storedReporte);
    }

    @Override
    public void estadoDenegado(String id) {
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        storedReporte.setEstado(EnumEstado.Denegado);
        storedReporte.setFechaActualizacion(new Date());
        reporteRepository.save(storedReporte);
    }

    private void validateReporteid(String id) {
        if (reporteRepository.findById(id).isPresent()) {
            throw new ValueConflictException("El reporte ya existe");
        }
    }
}