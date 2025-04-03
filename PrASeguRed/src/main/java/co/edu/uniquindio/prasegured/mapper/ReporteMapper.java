package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.model.Reporte;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReporteMapper {
    ReporteMapper INSTANCE = Mappers.getMapper(ReporteMapper.class);
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "status", constant = "ACTIVE")
    Reporte parseOf(ReporteRequest reporteRequest);
    ReporteDTO parseOf(Reporte reporte);
}
