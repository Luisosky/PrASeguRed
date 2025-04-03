package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReporteService {
    ReporteDTO save(ReporteRequest reporte);
    ReporteDTO update(String id, Reporte reporte);
    List<ReporteDTO> findAll();
    ReporteDTO findById(String id);
    void deleteById(String id);
}
