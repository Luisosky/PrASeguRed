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
public class ImagenReporte {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String reporteId;
    private String url;
    private EnumEstado estado;
}
