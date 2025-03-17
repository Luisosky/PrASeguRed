package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.ReportRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Reports", description = "API for report management")
public class ReportController {

    @PostMapping("/reportes")
    public ResponseEntity<Map<String, String>> createReport(@RequestBody ReportRequest request) {
        return new ResponseEntity<>(
                Map.of("message", "Reporte creado exitosamente."),
                HttpStatus.CREATED);
    }

    @PatchMapping("/reportes/{id}")
    public ResponseEntity<Map<String, String>> updateReport(
            @PathVariable String id,
            @RequestBody ReportRequest request) {
        return ResponseEntity.ok(Map.of("message", "Reporte actualizado."));
    }

    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<Map<String, String>> deleteReport(
            @PathVariable String id,
            @RequestBody Map<String, Boolean> confirmation) {
        return ResponseEntity.ok(Map.of("message", "Reporte eliminado correctamente."));
    }

    @PatchMapping("/reportes/{id}/prioridad")
    public ResponseEntity<Map<String, String>> prioritizeReport(@PathVariable String id) {

        return ResponseEntity.ok(Map.of("message", "Reporte priorizado."));
    }

    @PatchMapping("/reportes/{id}/resolucion")
    public ResponseEntity<Map<String, String>> resolveReport(@PathVariable String id) {

        return ResponseEntity.ok(Map.of("message", "Reporte resuelto correctamente."));
    }
}