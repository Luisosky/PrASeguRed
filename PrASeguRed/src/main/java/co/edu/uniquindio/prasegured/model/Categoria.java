package co.edu.uniquindio.prasegured.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Categorias")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categoria {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Indexed(unique = true)
    private String name;
    private String descripcion;
    private EnumEstado tipoCategoria;
    private String status;
}