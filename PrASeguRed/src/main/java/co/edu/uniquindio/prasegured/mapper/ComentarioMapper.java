package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComentarioMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "fechaPublicacion", expression = "java(new java.util.Date())")
    @Mapping(target = "likes", constant = "0")
    @Mapping(target = "dislikes", constant = "0")
    @Mapping(target = "estado", expression = "java(co.edu.uniquindio.prasegured.model.ESTADOREPORTE.Espera)")
    @Mapping(target = "usersLiked", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "usersDisliked", expression = "java(new java.util.ArrayList<>())")
    Comentario toComentario(ComentarioRequest request);

    ComentarioDTO toDTO(Comentario comentario);

    List<ComentarioDTO> toDTOList(List<Comentario> comentarios);
}