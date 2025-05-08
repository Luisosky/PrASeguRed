package co.edu.uniquindio.prasegured.services.unit;

import co.edu.uniquindio.prasegured.dto.CategoriaDTO;
import co.edu.uniquindio.prasegured.dto.CategoriaRequest;
import co.edu.uniquindio.prasegured.exception.ResourceNotFoundException;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import co.edu.uniquindio.prasegured.mapper.CategoriaMapper;
import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.repository.CategoriaRepository;
import co.edu.uniquindio.prasegured.service.CategoriaServiceImple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Profile("test")
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper categoriaMapper;

    @InjectMocks
    private CategoriaServiceImple categoriaService;

    private Categoria categoria;
    private CategoriaDTO categoriaDTO;
    private CategoriaRequest categoriaRequest;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId("cat01");
        categoria.setName("Categoria 1");
        categoria.setDescripcion("Descripción de categoría 1");
        categoria.setStatus(ESTADOREPORTE.Espera.toString());

        categoriaDTO = new CategoriaDTO(
                categoria.getId(),
                categoria.getName(),
                categoria.getDescripcion(),
                ESTADOREPORTE.Espera
        );

        categoriaRequest = new CategoriaRequest(
                "Categoria 1",
                "Descripción de categoría 1",
                ESTADOREPORTE.Espera
        );
    }

    @Test
    void testGuardarCategoria() {
        when(categoriaMapper.parseOf(categoriaRequest)).thenReturn(categoria);
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        when(categoriaMapper.toCategoriaDTO(categoria)).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.save(categoriaRequest);

        assertNotNull(result);
        assertEquals(categoriaDTO.id(), result.id());
        assertEquals(categoriaDTO.name(), result.name());

        verify(categoriaMapper).parseOf(categoriaRequest);
        verify(categoriaRepository).save(categoria);
        verify(categoriaMapper).toCategoriaDTO(categoria);
    }

    @Test
    void testGuardarCategoriaNombreDuplicado() {
        when(categoriaRepository.findByName(categoriaRequest.name())).thenReturn(Optional.of(categoria));

        assertThrows(ValueConflictException.class, () -> categoriaService.save(categoriaRequest));

        verify(categoriaRepository).findByName(categoriaRequest.name());
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void testActualizarCategoria() {
        when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(categoria)).thenReturn(categoria);
        when(categoriaMapper.toCategoriaDTO(categoria)).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.update(categoria.getId(), categoriaRequest);

        assertNotNull(result);
        assertEquals(categoriaDTO.id(), result.id());
        assertEquals(categoriaDTO.name(), result.name());

        verify(categoriaRepository).findById(categoria.getId());
        verify(categoriaRepository).save(categoria);
        verify(categoriaMapper).toCategoriaDTO(categoria);
    }

    @Test
    void testActualizarCategoriaNoEncontrada() {
        when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoriaService.update(categoria.getId(), categoriaRequest));

        verify(categoriaRepository).findById(categoria.getId());
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void testEliminarCategoria() {
        when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.of(categoria));

        categoriaService.deleteById(categoria.getId());

        assertEquals(ESTADOREPORTE.Eliminado.toString(), categoria.getStatus());

        verify(categoriaRepository).findById(categoria.getId());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void testEliminarCategoriaNoEncontrada() {
        when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoriaService.deleteById(categoria.getId()));

        verify(categoriaRepository).findById(categoria.getId());
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void testBuscarCategoriaPorId() {
        when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.of(categoria));
        when(categoriaMapper.toCategoriaDTO(categoria)).thenReturn(categoriaDTO);

        CategoriaDTO result = categoriaService.findById(categoria.getId());

        assertNotNull(result);
        assertEquals(categoriaDTO.id(), result.id());
        assertEquals(categoriaDTO.name(), result.name());

        verify(categoriaRepository).findById(categoria.getId());
        verify(categoriaMapper).toCategoriaDTO(categoria);
    }

    @Test
    void testBuscarCategoriaPorIdNoEncontrada() {
        when(categoriaRepository.findById(categoria.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoriaService.findById(categoria.getId()));

        verify(categoriaRepository).findById(categoria.getId());
        verify(categoriaMapper, never()).toCategoriaDTO(any(Categoria.class));
    }

    @Test
    void testListarTodasLasCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));
        when(categoriaMapper.toCategoriaDTO(categoria)).thenReturn(categoriaDTO);

        List<CategoriaDTO> result = categoriaService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoriaDTO.id(), result.get(0).id());

        verify(categoriaRepository).findAll();
        verify(categoriaMapper).toCategoriaDTO(categoria);
    }
}