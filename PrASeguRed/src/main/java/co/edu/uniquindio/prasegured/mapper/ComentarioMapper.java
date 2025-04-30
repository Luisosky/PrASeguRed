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
    @Mapping(target = "estado", expression = "java(ESTADOREPORTE.Espera)")
    Comentario toComentario(ComentarioRequest request);

    ComentarioDTO toComentarioDTO(Comentario comentario);
}

