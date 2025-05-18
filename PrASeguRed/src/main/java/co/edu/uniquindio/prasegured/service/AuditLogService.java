package co.edu.uniquindio.prasegured.service;

public interface AuditLogService {
    void registrarCambio(String entidad, Long entidadId, String accion, String estadoAnterior, String estadoNuevo, String realizadoPor);
}
