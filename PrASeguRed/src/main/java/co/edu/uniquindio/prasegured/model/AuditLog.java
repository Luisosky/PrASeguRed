package co.edu.uniquindio.prasegured.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "AuditLog")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuditLog {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private String entidad; // Usuario o Reporte
    private Long entidadId;
    private String accion; // Agregado o Cambio de estado
    private String estadoAnterior;
    private String estadoNuevo;
    private String realizadoPor;
    private LocalDateTime fechaHora;

}
