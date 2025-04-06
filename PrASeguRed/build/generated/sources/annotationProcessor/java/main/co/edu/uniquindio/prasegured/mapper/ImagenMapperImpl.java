package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.dto.ImagenRequest;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import co.edu.uniquindio.prasegured.model.Imagen;
import java.util.Arrays;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-06T13:47:47-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class ImagenMapperImpl implements ImagenMapper {

    @Override
    public Imagen parseOf(ImagenRequest imagenRequest) {
        if ( imagenRequest == null ) {
            return null;
        }

        Imagen imagen = new Imagen();

        imagen.setReporteId( imagenRequest.reporteId() );
        imagen.setUsuarioId( imagenRequest.usuarioId() );
        imagen.setNombre( imagenRequest.nombre() );
        byte[] content = imagenRequest.content();
        if ( content != null ) {
            imagen.setContent( Arrays.copyOf( content, content.length ) );
        }

        imagen.setId( java.util.UUID.randomUUID().toString() );
        imagen.setEstado( EnumEstado.Espera );

        return imagen;
    }

    @Override
    public ImagenDTO toImagenDTO(Imagen imagen) {
        if ( imagen == null ) {
            return null;
        }

        String id = null;
        String reporteId = null;
        String usuarioId = null;
        String nombre = null;
        byte[] content = null;
        EnumEstado estado = null;

        id = imagen.getId();
        reporteId = imagen.getReporteId();
        usuarioId = imagen.getUsuarioId();
        nombre = imagen.getNombre();
        byte[] content1 = imagen.getContent();
        if ( content1 != null ) {
            content = Arrays.copyOf( content1, content1.length );
        }
        estado = imagen.getEstado();

        ImagenDTO imagenDTO = new ImagenDTO( id, reporteId, usuarioId, nombre, content, estado );

        return imagenDTO;
    }
}
