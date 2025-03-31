package co.edu.uniquindio.prasegured.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "Categorias")

public class Categoria {
    @Id
    private String id;
    private String tipoCategoria;
    private String descripcion;
}
