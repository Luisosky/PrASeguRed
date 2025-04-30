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
    @Mapping(target = "estado", expression = "java(ESTADOREPORTE.Espera)")
    @Mapping(target = "fechaPublicacion", expression = "java(new java.util.Date())")
    @Mapping(target = "fechaActualizacion", expression = "java(new java.util.Date())")
    @Mapping(target = "likes", constant = "0")
    @Mapping(target = "dislikes", constant = "0")
    Reporte parseOf(ReporteRequest reporteRequest);

    ReporteDTO toReporteDTO(Reporte reporte);
}
