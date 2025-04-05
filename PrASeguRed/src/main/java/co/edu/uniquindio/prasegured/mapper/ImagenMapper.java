package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.dto.ImagenRequest;
import co.edu.uniquindio.prasegured.model.Imagen;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImagenMapper {
    ImagenMapper INSTANCE = Mappers.getMapper(ImagenMapper.class);

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "status", constant = "ACTIVE")
    Imagen parseOf(ImagenRequest imagenRequest);
    ImagenDTO toImagenDTO(Imagen imagen);

}
