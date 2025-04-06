package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.CategoriaDTO;
import co.edu.uniquindio.prasegured.dto.CategoriaRequest;
import co.edu.uniquindio.prasegured.model.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    Categoria parseOf(CategoriaRequest categoryRequest);
    CategoriaDTO toCategoriaDTO(Categoria categoria);
}