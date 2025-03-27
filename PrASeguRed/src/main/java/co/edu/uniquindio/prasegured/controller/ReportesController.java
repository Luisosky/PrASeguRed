package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReportesController {

    @Operation(
            summary = "Creación de reporte",
            description = "Crea un nuevo reporte en el sistema",
            operationId = "crearReporte"
    )
    @ApiResponse(responseCode = "201", description = "Reporte creado exitosamente.")
    @PostMapping("/reportes")
    public ResponseEntity<Void> crearReporte(@RequestBody ReporteDTO reporte) {
        // Implementación de creación de reporte
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Actualización de reporte",
            description = "Permite modificar un reporte existente",
            operationId = "actualizarReporte"
    )
    @ApiResponse(responseCode = "200", description = "Reporte actualizado.")
    @PatchMapping("/reportes/{id}")
    public ResponseEntity<Void> actualizarReporte(
            @PathVariable String id,
            @RequestBody ReporteDTO reporte) {
        // Implementación para actualizar reporte
        return ResponseEntity.ok().build();
    }

    // Añadir aquí el resto de endpoints de reportes
}