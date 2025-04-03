package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reportes")
public class ReportesController {

    @Autowired
    private ReporteService reporteService;
    @PostMapping
    public ReporteDTO create(@RequestBody Reporte reporte) {
        return reporteService.save(reporte);
    }

    @GetMapping
    public List<ReporteDTO> findAll() {
        return reporteService.findAll();
    }
    @GetMapping("/{id}")
    public ReporteDTO findById(@PathVariable("id") String id) {
        return reporteService.findById(id);
    }

    @PutMapping("/{id}")
    public ReporteDTO update(@PathVariable("id") String id, @RequestBody ReporteRequest reporte) {
        return reporteService.update(id, reporte);
    }

    @DeleteMapping("/{id}")
    public ReporteDTO delete(@PathVariable("id") String id) {
        return reporteService.deleteById(id);
    }

}