package co.edu.uniquindio.prasegured.dto;

import co.edu.uniquindio.prasegured.model.EnumEstado;

public record ImagenDTO(
        String id,
        String reporteId,
        String usuarioId,
        String nombre,
        byte[] content,
        EnumEstado estado){
}
