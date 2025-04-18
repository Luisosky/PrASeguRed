package co.edu.uniquindio.prasegured.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "Reportes")
@Getter
@Setter
public class Reporte {
    @Id
    private String id;
    private String idUsuario;
    private EnumEstado estado;
    private String creadorAnuncio;
    private String titulo;
    private Date fechaPublicacion;
    private Date fechaActualizacion;
    private String descripcion;
    private String ubicacion;
    private int likes;
    private int dislikes;
    private List<Categoria> categoria;
    private List<Location> locations;
}
