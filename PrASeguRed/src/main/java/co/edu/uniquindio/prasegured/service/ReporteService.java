package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReporteService {
    ReporteDTO save(ReporteRequest reporte);
    ReporteDTO update(String id, ReporteRequest reporte);
    List<ReporteDTO> findAll();
    ReporteDTO findById(String id);
    void deleteById(String id);
    void reporteCompleto(String id);
    void estadoDenegado(String id);
}
