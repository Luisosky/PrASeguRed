package co.edu.uniquindio.prasegured.dto;

import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.model.Location;

import java.util.Date;
import java.util.List;

public record ReporteRequest(
        String id,
        String idUsuario,
        String creadorAnuncio,
        String titulo,
        String descripcion,
        String ubicacion,
        List<Categoria> categoria,
        List<Location> locations
) {}