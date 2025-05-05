package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.Location;
import co.edu.uniquindio.prasegured.model.Reporte;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T10:17:53-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class ReporteMapperImpl implements ReporteMapper {

    @Override
    public Reporte parseOf(ReporteRequest reporteRequest) {
        if ( reporteRequest == null ) {
            return null;
        }

        Reporte reporte = new Reporte();

        reporte.setIdUsuario( reporteRequest.idUsuario() );
        reporte.setTitulo( reporteRequest.titulo() );
        reporte.setDescripcion( reporteRequest.descripcion() );
        reporte.setUbicacion( reporteRequest.ubicacion() );
        List<Categoria> list = reporteRequest.categoria();
        if ( list != null ) {
            reporte.setCategoria( new ArrayList<Categoria>( list ) );
        }
        List<Location> list1 = reporteRequest.locations();
        if ( list1 != null ) {
            reporte.setLocations( new ArrayList<Location>( list1 ) );
        }

        reporte.setId( java.util.UUID.randomUUID().toString() );
        reporte.setEstado( ESTADOREPORTE.Espera );
        reporte.setFechaPublicacion( new java.util.Date() );
        reporte.setFechaActualizacion( new java.util.Date() );
        reporte.setLikes( 0 );
        reporte.setDislikes( 0 );

        return reporte;
    }

    @Override
    public ReporteDTO toReporteDTO(Reporte reporte) {
        if ( reporte == null ) {
            return null;
        }

        String id = null;
        String idUsuario = null;
        ESTADOREPORTE estado = null;
        String creadorAnuncio = null;
        String titulo = null;
        Date fechaPublicacion = null;
        Date fechaActualizacion = null;
        String descripcion = null;
        String ubicacion = null;
        int likes = 0;
        int dislikes = 0;
        List<Categoria> categoria = null;
        List<Location> locations = null;

        id = reporte.getId();
        idUsuario = reporte.getIdUsuario();
        estado = reporte.getEstado();
        creadorAnuncio = reporte.getCreadorAnuncio();
        titulo = reporte.getTitulo();
        fechaPublicacion = reporte.getFechaPublicacion();
        fechaActualizacion = reporte.getFechaActualizacion();
        descripcion = reporte.getDescripcion();
        ubicacion = reporte.getUbicacion();
        likes = reporte.getLikes();
        dislikes = reporte.getDislikes();
        List<Categoria> list = reporte.getCategoria();
        if ( list != null ) {
            categoria = new ArrayList<Categoria>( list );
        }
        List<Location> list1 = reporte.getLocations();
        if ( list1 != null ) {
            locations = new ArrayList<Location>( list1 );
        }

        ReporteDTO reporteDTO = new ReporteDTO( id, idUsuario, estado, creadorAnuncio, titulo, fechaPublicacion, fechaActualizacion, descripcion, ubicacion, likes, dislikes, categoria, locations );

        return reporteDTO;
    }
}
