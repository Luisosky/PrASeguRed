package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.model.Reporte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReporteMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "fechaPublicacion", expression = "java(new java.util.Date())")
    @Mapping(target = "fechaActualizacion", expression = "java(new java.util.Date())")
    @Mapping(target = "likes", constant = "0")
    @Mapping(target = "dislikes", constant = "0")
    Reporte parseOf(ReporteRequest reporteRequest);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "idUsuario", target = "idUsuario")
    @Mapping(source = "estado", target = "estado")
    @Mapping(source = "creadorAnuncio", target = "creadorAnuncio")
    @Mapping(source = "titulo", target = "titulo")
    @Mapping(source = "fechaPublicacion", target = "fechaPublicacion")
    @Mapping(source = "fechaActualizacion", target = "fechaActualizacion")
    @Mapping(source = "descripcion", target = "descripcion")
    @Mapping(source = "ubicacion", target = "ubicacion")
    @Mapping(source = "likes", target = "likes")
    @Mapping(source = "dislikes", target = "dislikes")
    @Mapping(source = "categoria", target = "categoria")
    @Mapping(source = "locations", target = "locations")
    ReporteDTO toReporteDTO(Reporte reporte);
}