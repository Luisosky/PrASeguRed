package co.edu.uniquindio.prasegured.dto;

import java.util.List;

public record ReporteDTO(
        String titulo,
        Integer limiteEdad,
        String descripcion,
        String ubicacion,
        Boolean importante,
        Boolean resuelto,
        List<String> imagenes,
        String categoria
) {}