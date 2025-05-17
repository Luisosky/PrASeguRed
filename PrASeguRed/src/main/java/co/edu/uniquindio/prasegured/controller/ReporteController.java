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
import co.edu.uniquindio.prasegured.security.JwtService;
import co.edu.uniquindio.prasegured.service.ReporteService;

@RestController
@RequestMapping("/reportes")
@CrossOrigin("*")
public class ReporteController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ReporteService reporteService;

    @PostMapping
    public ResponseEntity<?> reporteCreacion(@RequestHeader("Authorization") String token, @RequestBody ReporteRequest reporteRequest) {
        try {
            // Extraer el token sin el prefijo "Bearer "
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            // Validar el token y extraer el correo electrónico del usuario
            String correo = jwtService.extractUsername(jwtToken);

            // Buscar el usuario por correo electrónico
            Usuario usuario = usuarioRepository.findByCorreo(correo);

            if (usuario != null) {
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
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token inválido o expirado", "detalle", e.getMessage()));
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
            // Verificar el token y autorización
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String correo = jwtService.extractUsername(jwtToken);
            Usuario usuario = usuarioRepository.findByCorreo(correo);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
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
            // Verificar el token y autorización
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String correo = jwtService.extractUsername(jwtToken);
            Usuario usuario = usuarioRepository.findByCorreo(correo);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
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
            // Verificar el token y autorización
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String correo = jwtService.extractUsername(jwtToken);
            Usuario usuario = usuarioRepository.findByCorreo(correo);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
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
            // Verificación opcional del token (puedes hacer que esta validación sea obligatoria si lo deseas)
            if (token != null && !token.isEmpty()) {
                String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
                String correo = jwtService.extractUsername(jwtToken);
                Usuario usuario = usuarioRepository.findByCorreo(correo);

                // Verificación adicional opcional: comprobar si el usuario solicitado coincide con el token
                // o si el usuario tiene permisos de administrador
                // if (!userId.equals(usuario.getId()) && !usuario.hasRole("ADMIN")) {
                //    return ResponseEntity.status(HttpStatus.FORBIDDEN)
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
            // Verificar el token y autorización
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String correo = jwtService.extractUsername(jwtToken);
            Usuario usuario = usuarioRepository.findByCorreo(correo);

            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Usuario no encontrado"));
            }

            reporteService.estadoDenegado(id);
            return ResponseEntity.ok(Map.of("message", "Reporte marcado como denegado"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al denegar reporte", "detalle", e.getMessage()));
        }
    }
}