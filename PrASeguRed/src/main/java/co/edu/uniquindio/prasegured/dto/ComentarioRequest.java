package co.edu.uniquindio.prasegured.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para la creación o actualización de comentarios
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ComentarioRequest(
        String idReporte,
        String idUsuario,
        Boolean anonimo,
        String nombre,
        String descripcion,  // Mantener como descripción para consistencia con el modelo
        String userImage  // Campo opcional para la imagen de perfil del usuario
) {
    /**
     * Constructor con valores por defecto para campos opcionales
     */
    public ComentarioRequest(String idReporte, String idUsuario, String descripcion) {
        this(idReporte, idUsuario, false, null, descripcion, null);
    }

    /**
     * Factory method para crear una instancia con un idReporte diferente
     */
    public ComentarioRequest withIdReporte(String newIdReporte) {
        return new ComentarioRequest(
                newIdReporte,
                this.idUsuario,
                this.anonimo,
                this.nombre,
                this.descripcion,
                this.userImage
        );
    }
}