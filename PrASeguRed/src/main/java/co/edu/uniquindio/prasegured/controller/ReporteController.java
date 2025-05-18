package co.edu.uniquindio.prasegured.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import co.edu.uniquindio.prasegured.service.ReporteService;
import co.edu.uniquindio.prasegured.service.TokenVerificationService;

@RestController
@RequestMapping("/reportes")
@CrossOrigin("*")
public class ReporteController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenVerificationService tokenVerificationService;

    @Autowired
    private ReporteService reporteService;

    @PostMapping
    public ResponseEntity<?> reporteCreacion(@RequestHeader("Authorization") String token, @RequestBody ReporteRequest reporteRequest) {
        try {
            // Validar token usando TokenVerificationService
            ResponseEntity<?> tokenValidationResponse = tokenVerificationService.validateTokenAndGetUser(token);
            if (tokenValidationResponse != null) {
                return tokenValidationResponse;
            }
            
            // Obtener el usuario desde el token
            Usuario usuario = tokenVerificationService.getActiveUserFromToken(token);
            
            // Asociar el ID del usuario al reporte
            ReporteRequest reporteConUsuario = new ReporteRequest(
                    reporteRequest.id(),
                    usuario.getId(), // Asignar el ID del usuario autenticado
                    reporteRequest.titulo(),
                    reporteRequest.descripcion(),
                    reporteRequest.categoria(),
                    reporteRequest.locations(),
                    reporteRequest.imagenes()
            );

            // Guardar el reporte usando el servicio
            ReporteDTO nuevoReporte = reporteService.save(reporteConUsuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of("message", "Reporte creado exitosamente.", "reporte", nuevoReporte)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Error al crear el reporte", "detalle", e.getMessage())
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<ReporteDTO>> getAllReportes() {
        List<ReporteDTO> reportes = reporteService.findAll();
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReporteById(@PathVariable String id) {
        try {
            ReporteDTO reporte = reporteService.findById(id);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Reporte no encontrado", "detalle", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReporte(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody ReporteRequest reporteRequest) {
        try {
            // Validar token usando TokenVerificationService
            ResponseEntity<?> tokenValidationResponse = tokenVerificationService.validateTokenAndGetUser(token);
            if (tokenValidationResponse != null) {
                return tokenValidationResponse;
            }

            // Actualizar el reporte
            ReporteDTO reporteActualizado = reporteService.update(id, reporteRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "Reporte actualizado exitosamente",
                    "reporte", reporteActualizado
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al actualizar reporte", "detalle", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReporte(
            @PathVariable String id,
            @RequestHeader("Authorization") String token) {
        try {
            // Validar token usando TokenVerificationService
            ResponseEntity<?> tokenValidationResponse = tokenVerificationService.validateTokenAndGetUser(token);
            if (tokenValidationResponse != null) {
                return tokenValidationResponse;
            }

            reporteService.deleteById(id);
            return ResponseEntity.ok(Map.of("message", "Reporte eliminado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Error al eliminar reporte", "detalle", e.getMessage()));
        }
    }

    @PutMapping("/{id}/completar")
    public ResponseEntity<?> completarReporte(
            @PathVariable String id,
            @RequestHeader("Authorization") String token) {
        try {
            // Validar token usando TokenVerificationService
            ResponseEntity<?> tokenValidationResponse = tokenVerificationService.validateTokenAndGetUser(token);
            if (tokenValidationResponse != null) {
                return tokenValidationResponse;
            }

            reporteService.reporteCompleto(id);
            return ResponseEntity.ok(Map.of("message", "Reporte marcado como completado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al completar reporte", "detalle", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReportesByUserId(@PathVariable String userId,
                                                 @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Verificación opcional del token
            if (token != null && !token.isEmpty()) {
                ResponseEntity<?> tokenValidationResponse = tokenVerificationService.validateTokenAndGetUser(token);
                if (tokenValidationResponse != null) {
                    return tokenValidationResponse;
                }
                
                // Aquí podrías agregar verificación adicional de permisos si lo deseas
                // Usuario usuario = tokenVerificationService.getActiveUserFromToken(token);
                // if (!userId.equals(usuario.getId()) && !usuario.hasRole("ADMIN")) {
                //     return ResponseEntity.status(HttpStatus.FORBIDDEN)
                //            .body(Map.of("error", "No tienes permiso para acceder a estos reportes"));
                // }
            }

            // Obtener los reportes del usuario
            List<ReporteDTO> reportes = reporteService.findByUserId(userId);

            if (reportes.isEmpty()) {
                return ResponseEntity.ok(List.of()); // Retornar lista vacía en lugar de error
            }

            return ResponseEntity.ok(reportes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener reportes del usuario",
                            "detalle", e.getMessage()));
        }
    }

    @PutMapping("/{id}/denegar")
    public ResponseEntity<?> denegarReporte(
            @PathVariable String id,
            @RequestHeader("Authorization") String token) {
        try {
            // Validar token usando TokenVerificationService
            ResponseEntity<?> tokenValidationResponse = tokenVerificationService.validateTokenAndGetUser(token);
            if (tokenValidationResponse != null) {
                return tokenValidationResponse;
            }

            reporteService.estadoDenegado(id);
            return ResponseEntity.ok(Map.of("message", "Reporte marcado como denegado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al denegar reporte", "detalle", e.getMessage()));
        }
    }
}