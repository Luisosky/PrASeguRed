package co.edu.uniquindio.prasegured.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "reportes")
@Getter
@Setter
public class Reporte {
    @Id
    private String id;
    private String idUsuario;
    private ESTADOREPORTE estado;
    private String titulo;
    private Date fechaPublicacion;
    private Date fechaActualizacion;
    private String descripcion;
    private int likes;
    private List<Categoria> categoria;
    private Location location;
    private List<Imagen> imagenes;

}
