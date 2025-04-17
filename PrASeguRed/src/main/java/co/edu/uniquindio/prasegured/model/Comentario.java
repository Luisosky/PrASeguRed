package co.edu.uniquindio.prasegured.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@Document(collection = "comentarios")
public class Comentario {
    @Id
    private String id;
    private String idReporte;
    private String idUsuario;
    private Boolean anonimo;
    private String nombre;
    private Date fechaPublicacion;
    private String descripcion;
    private int likes;
    private int dislikes;
    private EnumEstado estado;
}

