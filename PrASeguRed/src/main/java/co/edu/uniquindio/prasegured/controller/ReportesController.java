package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<ReporteDTO> create(@Valid @RequestBody ReporteRequest reporte) {
        // Lógica para guardar el reporte
        ReporteDTO savedReporte = reporteService.save(reporte);

        // Devolver el código de estado 201 Created junto con el reporte creado
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReporte);
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
    public ReporteDTO update(@PathVariable("id") String id, @Valid @RequestBody ReporteRequest reporte) {
        return reporteService.update(id, reporte);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        reporteService.deleteById(id);
    }

    @PutMapping("/completo/{id}")
    public ResponseEntity<Void> reporteCompleto(@PathVariable("id") String id) {
        reporteService.reporteCompleto(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/denegado/{id}")
    public ResponseEntity<Void> estadoDenegado(@PathVariable("id") String id) {
        reporteService.estadoDenegado(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}