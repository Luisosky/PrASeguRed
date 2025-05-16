package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.ImagenMetadataDTO;
import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.model.Reporte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReporteMapper {
    private static final Logger logger = LoggerFactory.getLogger(ReporteMapper.class);

    public ReporteDTO toReporteDTO(Reporte reporte) {
        if (reporte == null) {
            return null;
        }

        try {
            logger.debug("Mapeando reporte a DTO: {}", reporte.getId());

            // Verificar y evitar que contentType de imágenes se use en campos de texto
            String id = reporte.getId();
            String titulo = reporte.getTitulo();
            String descripcion = reporte.getDescripcion();
            String idUsuario = reporte.getIdUsuario();

            // VERIFICAR CAMPOS PARA EVITAR CONTENIDO MIME TYPE
            if (esMimeType(id)) {
                logger.warn("Se detectó un MIME type en el campo ID: {}", id);
                id = "id-" + System.currentTimeMillis();
            }

            if (esMimeType(titulo)) {
                logger.warn("Se detectó un MIME type en el campo título: {}", titulo);
                titulo = "Título no disponible";
            }

            if (esMimeType(descripcion)) {
                logger.warn("Se detectó un MIME type en el campo descripción: {}", descripcion);
                descripcion = "Descripción no disponible";
            }

            if (esMimeType(idUsuario)) {
                logger.warn("Se detectó un MIME type en el campo idUsuario: {}", idUsuario);
                idUsuario = "usuario-desconocido";
            }

            // Mapear imágenes con seguridad
            List<ImagenMetadataDTO> imagenesDTO = mapearImagenes(reporte.getImagenes());

            // Crear DTO con campos validados
            return new ReporteDTO(
                    id,
                    idUsuario,
                    reporte.getEstado(),
                    titulo,
                    reporte.getFechaPublicacion(),
                    reporte.getFechaActualizacion(),
                    descripcion,
                    reporte.getLikes(),
                    reporte.getCategoria(),
                    reporte.getLocation(),
                    imagenesDTO
            );
        } catch (Exception e) {
            logger.error("Error mapeando reporte a DTO: {}", e.getMessage());
            return createErrorDTO(reporte);
        }
    }

    private boolean esMimeType(String valor) {
        if (valor == null) return false;
        return valor.equals("image/jpeg") ||
                valor.equals("image/png") ||
                (valor.contains("/") && valor.startsWith("image/"));
    }

    // Método para convertir imágenes a DTOs de manera segura
    private List<ImagenMetadataDTO> mapearImagenes(List<Imagen> imagenes) {
        if (imagenes == null || imagenes.isEmpty()) {
            return Collections.emptyList();
        }

        return imagenes.stream()
                .map(img -> {
                    try {
                        // Crear DTO solo con metadatos, sin incluir el contenido binario completo
                        return new ImagenMetadataDTO(
                                img.getId(),
                                img.getReporteId(),
                                img.getUsuarioId(),
                                img.getNombre(),
                                img.getContent() != null ? img.getContent().length : 0,
                                determinarContentType(img),
                                "/api/imagenes/" + img.getId(),
                                img.getEstado()
                        );
                    } catch (Exception e) {
                        logger.error("Error mapeando imagen a DTO: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    private String determinarContentType(Imagen img) {
        // Si no podemos determinar el tipo, usamos un valor predeterminado seguro
        if (img == null || img.getNombre() == null) return "image/jpeg";

        String nombre = img.getNombre().toLowerCase();
        if (nombre.endsWith(".png")) return "image/png";
        if (nombre.endsWith(".gif")) return "image/gif";
        if (nombre.endsWith(".webp")) return "image/webp";

        // Por defecto asumimos JPEG
        return "image/jpeg";
    }

    // Crear un DTO de error para casos donde falle el mapeo
    private ReporteDTO createErrorDTO(Reporte reporte) {
        String id = reporte != null ? reporte.getId() : "error-" + System.currentTimeMillis();

        return new ReporteDTO(
                id,
                "error",
                null,
                "Error al cargar reporte",
                null,
                null,
                "No se pudieron cargar los datos del reporte correctamente",
                0,
                Collections.emptyList(),
                null,
                Collections.emptyList()
        );
    }
}