package co.edu.uniquindio.prasegured.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "comentarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {

    @Id
    private String id;
    private String idReporte;
    private String idUsuario;
    private String nombre;
    private Date fechaPublicacion;
    private String descripcion;
    private int likes;
    private int dislikes;
    private ESTADOREPORTE estado;

    // Estos campos son los que faltan en tu clase Comentario
    private List<String> usersLiked = new ArrayList<>();
    private List<String> usersDisliked = new ArrayList<>();

    // Soporte para comentarios anónimos
    private Boolean anonimo = false;

    // Campo para la imagen de perfil del usuario
    private String userImage;
}