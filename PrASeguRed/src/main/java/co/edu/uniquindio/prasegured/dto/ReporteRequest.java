package co.edu.uniquindio.prasegured.dto;

import java.util.List;

import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.model.Location;

public record ReporteRequest(
        String id,
        String idUsuario,
        String titulo,
        String descripcion,
        List<Categoria> categoria,
        Location locations,
        List<Imagen> imagenes

) {}