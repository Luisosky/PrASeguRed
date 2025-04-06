package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.model.Comentario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ComentarioMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "fechaPublicacion", expression = "java(new java.util.Date())")
    @Mapping(target = "estado", constant = "ACTIVO")
    @Mapping(target = "numeroCalificaciones", constant = "1")
    @Mapping(source = "idReporte", target = "id_Reporte")
    @Mapping(source = "idUsuario", target = "id_usuario")
    Comentario toComentario(ComentarioRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "id_Reporte", target = "idReporte")
    @Mapping(source = "id_usuario", target = "idUsuario")
    ComentarioDTO toComentarioDTO(Comentario comentario);
}