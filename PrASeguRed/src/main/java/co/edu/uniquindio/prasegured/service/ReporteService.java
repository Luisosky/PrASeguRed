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

    public Reporte actualizarReporte(String id, Reporte reporte) {
        // Implementa la lógica para actualizar un reporte
    }

    public List<Reporte> getAllReportes() {
        return reporteRepository.findAll();
    }

    public void eliminarReporte(String id) {
        // Implementa la lógica para eliminar un reporte
    }
}
