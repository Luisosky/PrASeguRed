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
    @Mapping(target = "estado", expression = "java(EnumEstado.Espera)")
    @Mapping(target = "numeroCalificaciones", constant = "1")
    @Mapping(source = "idReporte", target = "idReporte")
    @Mapping(source = "idUsuario", target = "idUsuario")
    Comentario toComentario(ComentarioRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "idReporte", target = "idReporte")
    @Mapping(source = "idUsuario", target = "idUsuario")
    ComentarioDTO toComentarioDTO(Comentario comentario);
}
