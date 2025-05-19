package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.NotificacionDTO;
import co.edu.uniquindio.prasegured.dto.NotificacionRequest;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificacionService {
    
    // Crear una nueva notificación
    NotificacionDTO crearNotificacion(NotificacionRequest notificacionRequest);
    
    // Buscar notificaciones por ID de usuario
    List<NotificacionDTO> buscarNotificacionesPorUsuario(String usuarioId);
    
    // Buscar notificaciones por ID de usuario con paginación
    Page<NotificacionDTO> buscarNotificacionesPorUsuario(String usuarioId, Pageable pageable);
    
    // Buscar notificaciones por ID de usuario y estado de lectura
    Page<NotificacionDTO> buscarNotificacionesPorUsuarioYEstado(String usuarioId, boolean leido, Pageable pageable);
    
    // Marcar una notificación como leída
    NotificacionDTO marcarNotificacionComoLeida(String notificacionId);
    
    // Marcar todas las notificaciones de un usuario como leídas
    void marcarTodasComoLeidas(String usuarioId);
    
    // Buscar una notificación por su ID
    NotificacionDTO buscarNotificacionPorId(String notificacionId);
    
    // Crear notificación de reporte cercano para usuarios 
    void notificarReporteCercano(String reporteId, double latitud, double longitud, double distanciaMaximaKm);
    
    // Notificar cambio de estado de un reporte a su creador
    void notificarCambioEstadoReporte(String reporteId, ESTADOREPORTE nuevoEstado);
}