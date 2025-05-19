package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.NotificacionDTO;
import co.edu.uniquindio.prasegured.dto.NotificacionRequest;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.ROL;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import co.edu.uniquindio.prasegured.security.JwtService;
import co.edu.uniquindio.prasegured.service.NotificacionService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/notificacion")
@CrossOrigin("*")
public class NotificacionController {

    private final Logger logger = LoggerFactory.getLogger(NotificacionController.class);
    
    @Autowired
    private NotificacionService notificacionService;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ReporteRepository reporteRepository;
    
    @PostMapping
    public ResponseEntity<?> crearNotificacion(
            @RequestHeader("Authorization") String token,
            @RequestBody NotificacionRequest notificacionRequest) {
        try {
            // Verificar autenticación y autorización
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String correoUsuario = jwtService.extractUsername(jwtToken);
            Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);
            
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Usuario no autenticado"));
            }
            
            // Solo admins y moderadores pueden crear notificaciones manualmente
            if (!usuario.getRol().equals(ROL.ADMINISTRADOR.toString())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permisos para crear notificaciones"));
            }
            
            NotificacionDTO notificacionDTO = notificacionService.crearNotificacion(notificacionRequest);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Notificación enviada correctamente.",
                            "notificacion", notificacionDTO
                    ));
        } catch (Exception e) {
            logger.error("Error al crear notificación: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear la notificación", "detalle", e.getMessage()));
        }
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerNotificacionesPorUsuario(
            @PathVariable String usuarioId,
            @RequestParam(required = false) Boolean leido,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String token) {
        try {
            // Verificar autenticación
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String correoUsuario = jwtService.extractUsername(jwtToken);
            Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);
            
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Usuario no autenticado"));
            }
            
            // Sólo permitir ver tus propias notificaciones o eres admin
            if (!usuario.getId().equals(usuarioId) && !usuario.getRol().equals(ROL.ADMINISTRADOR.toString())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permisos para ver estas notificaciones"));
            }
            
            Pageable pageable = PageRequest.of(page, size, Sort.by("fechaCreacion").descending());
            Page<NotificacionDTO> notificaciones;
            
            if (leido != null) {
                notificaciones = notificacionService.buscarNotificacionesPorUsuarioYEstado(usuarioId, leido, pageable);
            } else {
                notificaciones = notificacionService.buscarNotificacionesPorUsuario(usuarioId, pageable);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("content", notificaciones.getContent());
            response.put("totalPages", notificaciones.getTotalPages());
            response.put("totalElements", notificaciones.getTotalElements());
            response.put("last", notificaciones.isLast());
            response.put("size", notificaciones.getSize());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al obtener notificaciones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener notificaciones", "detalle", e.getMessage()));
        }
    }
    
    @PatchMapping("/{notificacionId}/leer")
    public ResponseEntity<?> marcarNotificacionComoLeida(
            @PathVariable String notificacionId,
            @RequestHeader("Authorization") String token) {
        try {
            // Verificar autenticación
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String correoUsuario = jwtService.extractUsername(jwtToken);
            Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);
            
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Usuario no autenticado"));
            }
            
            // Obtener la notificación
            NotificacionDTO notificacion = notificacionService.buscarNotificacionPorId(notificacionId);
            
            // Verificar si la notificación pertenece al usuario
            if (!notificacion.usuarioId().equals(usuario.getId()) && !usuario.getRol().equals(ROL.ADMINISTRADOR.toString())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permisos para modificar esta notificación"));
            }
            
            NotificacionDTO notificacionActualizada = notificacionService.marcarNotificacionComoLeida(notificacionId);
            
            return ResponseEntity.ok(Map.of(
                    "message", "Notificación marcada como leída",
                    "notificacion", notificacionActualizada
            ));
        } catch (Exception e) {
            logger.error("Error al marcar notificación como leída: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al marcar notificación como leída", "detalle", e.getMessage()));
        }
    }
    
    @PatchMapping("/usuario/{usuarioId}/leer-todas")
    public ResponseEntity<?> marcarTodasComoLeidas(
            @PathVariable String usuarioId,
            @RequestHeader("Authorization") String token) {
        try {
            // Verificar autenticación
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String correoUsuario = jwtService.extractUsername(jwtToken);
            Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);
            
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Usuario no autenticado"));
            }
            
            // Sólo permitir marcar tus propias notificaciones o eres admin
            if (!usuario.getId().equals(usuarioId) && !usuario.getRol().equals(ROL.ADMINISTRADOR.toString())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permisos para modificar estas notificaciones"));
            }
            
            notificacionService.marcarTodasComoLeidas(usuarioId);
            
            return ResponseEntity.ok(Map.of("message", "Todas las notificaciones marcadas como leídas"));
        } catch (Exception e) {
            logger.error("Error al marcar todas las notificaciones como leídas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al marcar notificaciones como leídas", "detalle", e.getMessage()));
        }
    }
    
    @PostMapping("/reportes-cercanos")
    public ResponseEntity<?> notificarReportesCercanos(
            @RequestBody Map<String, Object> request,
            @RequestHeader("Authorization") String token) {
        try {
            // Verificar autenticación
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
            String correoUsuario = jwtService.extractUsername(jwtToken);
            Usuario usuario = usuarioRepository.findByCorreo(correoUsuario);
            
            if (usuario == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Usuario no autenticado"));
            }
            
            // Solo admins pueden usar esta función
            if (!usuario.getRol().equals(ROL.ADMINISTRADOR.toString())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "No tienes permisos para usar esta función"));
            }
            
            // Obtener parámetros
            String reporteId = (String) request.get("reporteId");
            Double latitud = Double.valueOf(request.get("latitud").toString());
            Double longitud = Double.valueOf(request.get("longitud").toString());
            Double distanciaMaximaKm = request.get("distanciaMaximaKm") != null ? 
                Double.valueOf(request.get("distanciaMaximaKm").toString()) : 5.0; // Por defecto 5 km
            
            // Verificar que el reporte existe
            Optional<Reporte> reporteOpt = reporteRepository.findById(reporteId);
            if (reporteOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Reporte no encontrado"));
            }
            
            // Notificar a los usuarios cercanos
            notificacionService.notificarReporteCercano(reporteId, latitud, longitud, distanciaMaximaKm);
            
            return ResponseEntity.ok(Map.of("message", "Notificaciones enviadas a usuarios cercanos"));
        } catch (Exception e) {
            logger.error("Error al notificar reportes cercanos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al notificar reportes cercanos", "detalle", e.getMessage()));
        }
    }
}