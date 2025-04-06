package co.edu.uniquindio.prasegured.mapper;

import co.edu.uniquindio.prasegured.dto.CategoriaDTO;
import co.edu.uniquindio.prasegured.dto.CategoriaRequest;
import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-06T16:38:26-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class CategoriaMapperImpl implements CategoriaMapper {

    @Override
    public Categoria parseOf(CategoriaRequest categoryRequest) {
        if ( categoryRequest == null ) {
            return null;
        }

        Categoria categoria = new Categoria();

        categoria.setName( categoryRequest.name() );
        categoria.setDescripcion( categoryRequest.descripcion() );
        if ( categoryRequest.tipoCategoria() != null ) {
            categoria.setTipoCategoria( categoryRequest.tipoCategoria().name() );
        }

        return categoria;
    }

    @Override
    public CategoriaDTO toCategoriaDTO(Categoria categoria) {
        if ( categoria == null ) {
            return null;
        }

        String id = null;
        String name = null;
        String descripcion = null;
        EnumEstado tipoCategoria = null;

        id = categoria.getId();
        name = categoria.getName();
        descripcion = categoria.getDescripcion();
        if ( categoria.getTipoCategoria() != null ) {
            tipoCategoria = Enum.valueOf( EnumEstado.class, categoria.getTipoCategoria() );
        }

        CategoriaDTO categoriaDTO = new CategoriaDTO( id, name, descripcion, tipoCategoria );

        return categoriaDTO;
    }
}
