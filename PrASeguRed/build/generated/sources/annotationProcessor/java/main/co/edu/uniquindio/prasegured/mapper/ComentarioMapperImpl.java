package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import java.util.Date;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-20T21:21:36-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 21.0.4 (Amazon.com Inc.)"
)
@Component
public class ComentarioMapperImpl implements ComentarioMapper {

    @Override
    public Comentario toComentario(ComentarioRequest request) {
        if ( request == null ) {
            return null;
        }

        Comentario comentario = new Comentario();

        comentario.setIdReporte( request.idReporte() );
        comentario.setIdUsuario( request.idUsuario() );
        comentario.setAnonimo( request.anonimo() );
        comentario.setNombre( request.nombre() );
        comentario.setDescripcion( request.descripcion() );
        comentario.setLikes( request.likes() );
        comentario.setDislikes( request.dislikes() );

        comentario.setId( java.util.UUID.randomUUID().toString() );
        comentario.setFechaPublicacion( new java.util.Date() );
        comentario.setEstado( EnumEstado.Espera );

        return comentario;
    }

    @Override
    public ComentarioDTO toComentarioDTO(Comentario comentario) {
        if ( comentario == null ) {
            return null;
        }

        String id = null;
        String idReporte = null;
        String idUsuario = null;
        Boolean anonimo = null;
        String nombre = null;
        Date fechaPublicacion = null;
        String descripcion = null;
        int likes = 0;
        int dislikes = 0;
        EnumEstado estado = null;

        id = comentario.getId();
        idReporte = comentario.getIdReporte();
        idUsuario = comentario.getIdUsuario();
        anonimo = comentario.getAnonimo();
        nombre = comentario.getNombre();
        fechaPublicacion = comentario.getFechaPublicacion();
        descripcion = comentario.getDescripcion();
        likes = comentario.getLikes();
        dislikes = comentario.getDislikes();
        estado = comentario.getEstado();

        ComentarioDTO comentarioDTO = new ComentarioDTO( id, idReporte, idUsuario, anonimo, nombre, fechaPublicacion, descripcion, likes, dislikes, estado );

        return comentarioDTO;
    }
}
