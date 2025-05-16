package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.ImagenMetadataDTO;
import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.model.Reporte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ReporteMapper {

    @Mapping(target = "imagenes", source = "imagenes", qualifiedByName = "mapToImagenMetadataDTO")
    ReporteDTO toReporteDTO(Reporte reporte);

    // Otros mapeos existentes...

    @Named("mapToImagenMetadataDTO")
    default List<ImagenMetadataDTO> mapToImagenMetadataDTO(List<Imagen> imagenes) {
        if (imagenes == null) {
            return null;
        }

        return imagenes.stream()
                .map(imagen -> {
                    String contentType = determinarContentType(imagen.getNombre());
                    long contentLength = imagen.getContent() != null ? imagen.getContent().length : 0;
                    String imageUrl = null;

                    if (imagen.getId() != null) {
                        imageUrl = "/api/imagenes/" + imagen.getId();
                    } else if (imagen.getReporteId() != null) {
                        // Construir URL por índice (se necesita buscar el índice)
                        // Este es un enfoque simplificado
                        imageUrl = "/api/reportes-imagenes/" + imagen.getReporteId() + "/imagen/0";
                    }

                    return new ImagenMetadataDTO(
                            imagen.getId(),
                            imagen.getReporteId(),
                            imagen.getUsuarioId(),
                            imagen.getNombre(),
                            contentLength,
                            contentType,
                            imageUrl,
                            imagen.getEstado()
                    );
                })
                .collect(Collectors.toList());
    }

    default String determinarContentType(String nombreArchivo) {
        if (nombreArchivo == null) {
            return "image/jpeg";
        }

        nombreArchivo = nombreArchivo.toLowerCase();

        if (nombreArchivo.endsWith(".png")) {
            return "image/png";
        } else if (nombreArchivo.endsWith(".gif")) {
            return "image/gif";
        } else if (nombreArchivo.endsWith(".webp")) {
            return "image/webp";
        } else if (nombreArchivo.endsWith(".svg")) {
            return "image/svg+xml";
        } else {
            return "image/jpeg";
        }
    }
}