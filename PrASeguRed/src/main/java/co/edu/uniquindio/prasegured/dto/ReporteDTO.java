package co.edu.uniquindio.prasegured.dto;
import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import co.edu.uniquindio.prasegured.model.Location;

import java.util.Date;
import java.util.List;

public record ReporteDTO(
        String id,
        String idUsuario,
        EnumEstado estado,
        String creadorAnuncio,
        String titulo,
        Date fechaPublicacion,
        Date fechaActualizacion,
        String descripcion,
        String ubicacion,
        int likes,
        int dislikes,
        List<Categoria> categoria,
        List<Location> locations
) {}