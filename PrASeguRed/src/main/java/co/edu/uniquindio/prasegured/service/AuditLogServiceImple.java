package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.model.AuditLog;
import co.edu.uniquindio.prasegured.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuditLogServiceImple implements AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;


    @Override
    public void registrarCambio(String entidad, Long entidadId, String accion, String estadoAnterior, String estadoNuevo, String realizadoPor) {
        AuditLog log = new AuditLog();
        log.setEntidad(entidad);
        log.setEntidadId(entidadId);
        log.setAccion(accion);
        log.setEstadoAnterior(estadoAnterior);
        log.setEstadoNuevo(estadoNuevo);
        log.setRealizadoPor(realizadoPor);
        log.setFechaHora(LocalDateTime.now());
        auditLogRepository.save(log);
    }
}
