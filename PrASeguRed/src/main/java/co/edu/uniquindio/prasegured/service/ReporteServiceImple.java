package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.mapper.ReporteMapper;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReporteServiceImple implements ReporteService {
    private final ReporteRepository reporteRepository;
    private final ReporteMapper reporteMapper;

    @Override
    public ReporteDTO save(ReporteRequest reporte) {
        var newReporte = reporteMapper.parseOf(reporte);
        reporteRepository(reporte.id());
        return reporteMapper.toReporteDTO(
                reporteRepository.save(newReporte)
        );
    }

    @Override
    public ReporteDTO update(String id, ReporteRequest reporte) {
        return null;
    }

    @Override
    public List<ReporteDTO> findAll() {
        return List.of();
    }

    @Override
    public ReporteDTO findById(String id) {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }
    private void validateReporteid(String id) {
        if (reporteRepository.findById(id).isPresent()) {
            throw new ValueConflictException("El reporte ya existe");
        }
    }
}