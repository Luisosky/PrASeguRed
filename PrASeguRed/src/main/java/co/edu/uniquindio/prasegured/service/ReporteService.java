package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private ReporteRepository reporteRepository;

    public Reporte registrarReporte(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    public List<Reporte> getAllReportes() {
        return reporteRepository.findAll();
    }
}
