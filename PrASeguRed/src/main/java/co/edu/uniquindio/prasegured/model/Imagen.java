package co.edu.uniquindio.prasegured.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Imagenes")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Imagen {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String reporteId;
    private String usuarioId;
    private String nombre;
    private byte[] content;
    private EnumEstado estado;
}
