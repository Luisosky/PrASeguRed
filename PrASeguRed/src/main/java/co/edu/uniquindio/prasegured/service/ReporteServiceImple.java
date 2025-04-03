package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.mapper.ReporteMapper;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;

public class ReporteServiceImple implements ReporteService {
    private final ReporteRepository reporteRepository;
    private final ReporteMapper reporteMapper;

    @Override
    public ReporteDTO save(ReporteRequest reporte) {
        var newReporte = reporteMapper.parseOf(reporte);
        validateReporteName(reporte.id());
        return reporteMapper.toReporteDTO(
                reporteRepository.save(newReporte)
        );
    }
}
