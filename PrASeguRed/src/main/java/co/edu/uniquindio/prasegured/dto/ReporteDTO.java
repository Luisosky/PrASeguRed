package co.edu.uniquindio.prasegured.dto;

import java.util.Date;
import java.util.List;

import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.Location;

public record ReporteDTO(
        String id,
        String idUsuario,
        ESTADOREPORTE estado,
        String titulo,
        Date fechaPublicacion,
        Date fechaActualizacion,
        String descripcion,
        int likes,
        List<Categoria> categoria,
        Location locations,
        List<ImagenMetadataDTO> imagenes  // Cambiado de List<Imagen> a List<ImagenMetadataDTO>
) {}