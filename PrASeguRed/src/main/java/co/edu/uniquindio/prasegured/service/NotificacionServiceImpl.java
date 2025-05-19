package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.NotificacionDTO;
import co.edu.uniquindio.prasegured.dto.NotificacionRequest;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.Location;
import co.edu.uniquindio.prasegured.model.Notificacion;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.NotificacionRepository;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificacionServiceImpl implements NotificacionService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificacionServiceImpl.class);
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ReporteRepository reporteRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public NotificacionDTO crearNotificacion(NotificacionRequest notificacionRequest) {
        // Validar que el usuario existe
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(notificacionRequest.usuarioId());
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + notificacionRequest.usuarioId());
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Crear la notificación
        Notificacion notificacion = new Notificacion(
            notificacionRequest.usuarioId(),
            notificacionRequest.titulo(),
            notificacionRequest.categoria(),
            notificacionRequest.contenido(),
            notificacionRequest.reporteId()
        );
        
        // Guardar la notificación
        notificacion = notificacionRepository.save(notificacion);
        
        // Enviar correo electrónico si el usuario tiene preferencias de correo
        if (usuario.getPreferencias() != null && usuario.getPreferencias().contains("EMAIL")) {
            try {
                emailService.enviarNotificacionPorEmail(
                    usuario.getCorreo(), 
                    notificacion.getTitulo(), 
                    notificacion.getContenido()
                );
                logger.info("Correo de notificación enviado a: {}", usuario.getCorreo());
            } catch (Exception e) {
                logger.error("Error al enviar correo de notificación: {}", e.getMessage());
            }
        }
        
        return convertirADTO(notificacion);
    }
    
    @Override
    public List<NotificacionDTO> buscarNotificacionesPorUsuario(String usuarioId) {
        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioId(usuarioId);
        return notificaciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Page<NotificacionDTO> buscarNotificacionesPorUsuario(String usuarioId, Pageable pageable) {
        Page<Notificacion> notificaciones = notificacionRepository.findByUsuarioId(usuarioId, pageable);
        return notificaciones.map(this::convertirADTO);
    }
    
    @Override
    public Page<NotificacionDTO> buscarNotificacionesPorUsuarioYEstado(String usuarioId, boolean leido, Pageable pageable) {
        Page<Notificacion> notificaciones = notificacionRepository.findByUsuarioIdAndLeido(usuarioId, leido, pageable);
        return notificaciones.map(this::convertirADTO);
    }
    
    @Override
    public NotificacionDTO marcarNotificacionComoLeida(String notificacionId) {
        Optional<Notificacion> notificacionOpt = notificacionRepository.findById(notificacionId);
        if (notificacionOpt.isEmpty()) {
            throw new RuntimeException("Notificación no encontrada con ID: " + notificacionId);
        }
        
        Notificacion notificacion = notificacionOpt.get();
        notificacion.setLeido(true);
        
        notificacion = notificacionRepository.save(notificacion);
        
        return convertirADTO(notificacion);
    }
    
    @Override
    public void marcarTodasComoLeidas(String usuarioId) {
        List<Notificacion> notificaciones = notificacionRepository.findByUsuarioId(usuarioId);
        for (Notificacion notificacion : notificaciones) {
            if (!notificacion.isLeido()) {
                notificacion.setLeido(true);
                notificacionRepository.save(notificacion);
            }
        }
    }
    
    @Override
    public NotificacionDTO buscarNotificacionPorId(String notificacionId) {
        Optional<Notificacion> notificacionOpt = notificacionRepository.findById(notificacionId);
        if (notificacionOpt.isEmpty()) {
            throw new RuntimeException("Notificación no encontrada con ID: " + notificacionId);
        }
        
        return convertirADTO(notificacionOpt.get());
    }
    
    @Override
    public void notificarReporteCercano(String reporteId, double latitud, double longitud, double distanciaMaximaKm) {
        Optional<Reporte> reporteOpt = reporteRepository.findById(reporteId);
        if (reporteOpt.isEmpty()) {
            logger.error("Reporte no encontrado con ID: {}", reporteId);
            return;
        }
        
        Reporte reporte = reporteOpt.get();
        
        // Verificar que el reporte tenga ubicación válida
        if (reporte.getLocation() == null) {
            logger.warn("El reporte {} no tiene ubicación", reporteId);
            return;
        }
        
        // Obtener todos los usuarios activos
        List<Usuario> usuarios = usuarioRepository.findUsuariosActivos();
        
        for (Usuario usuario : usuarios) {
            // No notificar al creador del reporte
            if (usuario.getId().equals(reporte.getIdUsuario())) {
                continue;
            }
            
            // Verificar si el usuario tiene ubicaciones registradas
            List<Location> ubicacionesUsuario = usuario.getLocations();
            if (ubicacionesUsuario == null || ubicacionesUsuario.isEmpty()) {
                continue;
            }
            
            // Verificar cada ubicación del usuario
            for (Location ubicacion : ubicacionesUsuario) {
                double distancia = calcularDistancia(
                    ubicacion.getLat(), 
                    ubicacion.getLng(), 
                    reporte.getLocation().getLat(), 
                    reporte.getLocation().getLng()
                );
                
                // Si el reporte está dentro del rango de distancia
                if (distancia <= distanciaMaximaKm) {
                    // Formatear la distancia
                    String distanciaFormateada = formatearDistancia(distancia);
                    
                    // Usar la dirección del usuario como referencia si está disponible
                    String ubicacionReferencia = usuario.getDireccion() != null ? 
                        "tu dirección registrada" : 
                        "tu ubicación";
                    
                    // Crear notificación
                    Notificacion notificacion = new Notificacion(
                        usuario.getId(),
                        "Reporte cercano a tu ubicación",
                        "Información",
                        "Hay un reporte de \"" + reporte.getTitulo() + "\" a " + distanciaFormateada + 
                        " de " + ubicacionReferencia + ".",
                        reporte.getId()
                    );
                    
                    notificacionRepository.save(notificacion);
                    
                    // Enviar correo electrónico si el usuario tiene preferencias de correo
                    if (usuario.getPreferencias() != null && usuario.getPreferencias().contains("EMAIL")) {
                        try {
                            emailService.enviarNotificacionPorEmail(
                                usuario.getCorreo(), 
                                notificacion.getTitulo(), 
                                notificacion.getContenido()
                            );
                        } catch (Exception e) {
                            logger.error("Error al enviar correo de notificación: {}", e.getMessage());
                        }
                    }
                    
                    // Una vez que encontramos una ubicación cercana, no necesitamos verificar las demás
                    break;
                }
            }
        }
    }
    
    @Override
    public void notificarCambioEstadoReporte(String reporteId, ESTADOREPORTE nuevoEstado) {
        Optional<Reporte> reporteOpt = reporteRepository.findById(reporteId);
        if (reporteOpt.isEmpty()) {
            logger.error("Reporte no encontrado con ID: {}", reporteId);
            return;
        }
        
        Reporte reporte = reporteOpt.get();
        
        // Obtener el usuario creador del reporte
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(reporte.getIdUsuario());
        if (usuarioOpt.isEmpty()) {
            logger.error("Usuario no encontrado con ID: {}", reporte.getIdUsuario());
            return;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Preparar el título y contenido según el nuevo estado
        String titulo = "Tu reporte ha sido actualizado";
        String contenido = "";
        
        switch (nuevoEstado) {
            case Publicado:
                titulo = "Tu reporte ha sido aprobado";
                contenido = "Buenas noticias! Tu reporte \"" + reporte.getTitulo() + "\" ha sido revisado y aprobado. "
                         + "Ya está visible para todos los usuarios de la plataforma.";
                break;
            case Completado:
                titulo = "Tu reporte ha sido completado";
                contenido = "Tu reporte \"" + reporte.getTitulo() + "\" ha sido marcado como completado. "
                         + "Gracias por tu participación en mejorar nuestra comunidad.";
                break;
            case Denegado:
                titulo = "Tu reporte ha sido rechazado";
                contenido = "Lo sentimos, tu reporte \"" + reporte.getTitulo() + "\" ha sido revisado y no cumple "
                         + "con los criterios para ser publicado. Puedes intentar con información más detallada.";
                break;
            default:
                contenido = "Tu reporte \"" + reporte.getTitulo() + "\" ha cambiado su estado a " + nuevoEstado + ".";
        }
        
        // Crear la notificación
        Notificacion notificacion = new Notificacion(
            usuario.getId(),
            titulo,
            "Actualización",
            contenido,
            reporteId
        );
        
        notificacionRepository.save(notificacion);
        
        // Enviar correo electrónico si el usuario tiene preferencias de correo
        if (usuario.getPreferencias() != null && usuario.getPreferencias().contains("EMAIL")) {
            try {
                emailService.enviarNotificacionPorEmail(
                    usuario.getCorreo(), 
                    notificacion.getTitulo(), 
                    notificacion.getContenido()
                );
            } catch (Exception e) {
                logger.error("Error al enviar correo de notificación: {}", e.getMessage());
            }
        }
    }
    
    // Método para convertir entidad a DTO
    private NotificacionDTO convertirADTO(Notificacion notificacion) {
        return new NotificacionDTO(
            notificacion.getId(),
            notificacion.getUsuarioId(),
            notificacion.getTitulo(),
            notificacion.getCategoria(),
            notificacion.getContenido(),
            notificacion.isLeido(),
            notificacion.getFechaCreacion(),
            notificacion.getReporteId()
        );
    }
    
    // Método para calcular distancia entre dos coordenadas (Fórmula de Haversine)
    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Radio de la Tierra en km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * 
                   Math.sin(dLon/2) * Math.sin(dLon/2);
                   
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
        return R * c; // Distancia en km
    }
    
    // Método para formatear la distancia
    private String formatearDistancia(double distancia) {
        if (distancia < 1) {
            return Math.round(distancia * 1000) + " metros";
        } else {
            return String.format("%.1f km", distancia);
        }
    }
}