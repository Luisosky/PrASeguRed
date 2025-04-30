package co.edu.uniquindio.prasegured.services.integration;

import co.edu.uniquindio.prasegured.data.TestDataLoaderCategorias;
import co.edu.uniquindio.prasegured.dto.CategoriaDTO;
import co.edu.uniquindio.prasegured.dto.CategoriaRequest;
import co.edu.uniquindio.prasegured.exception.ResourceNotFoundException;
import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.repository.CategoriaRepository;
import co.edu.uniquindio.prasegured.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoriaServiceTest {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, Categoria> categorias;

    @BeforeEach
    void setUp() {
        // Cargar datos de prueba para las categorías
        categorias = TestDataLoaderCategorias.loadTestData(categoriaRepository, mongoTemplate);
    }

    @Test
    void testSaveCategoria() {
        var categoria = new CategoriaRequest("celulares","tecnologia", ESTADOREPORTE.Espera);
        CategoriaDTO savedCategoria = categoriaService.save(categoria);
        assertNotNull(savedCategoria.id());
        assertEquals(categoria.descripcion(), savedCategoria.descripcion());
        assertEquals(ESTADOREPORTE.Espera, savedCategoria.tipoCategoria());
    }

    @Test
    void testDeleteCategoria() {
        var categoria = categorias.values().stream().findAny().orElseThrow();
        categoriaService.deleteById(categoria.getId());
        var deletedCategoria = categoriaRepository.findById(categoria.getId()).orElseThrow();
        assertEquals(""+ ESTADOREPORTE.Eliminado, deletedCategoria.getStatus());
    }

    @Test
    void testUpdateCategoria() {
        var categoria = categorias.values().stream().findAny().orElseThrow();
        var categoriaActualizada = new CategoriaRequest("celulares","tecnologia", ESTADOREPORTE.Espera);
        CategoriaDTO updatedCategoria = categoriaService.update(categoria.getId(), categoriaActualizada);
        assertNotNull(updatedCategoria);
        assertEquals(categoriaActualizada.name(), updatedCategoria.name());
    }

    @Test
    void testFindAllCategorias() {
        var result = categoriaService.findAll();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(categorias.size(), result.size());
    }

    @Test
    void testDeleteCategoriaThrowsException() {
        String nonExistentId = "nonexistentId";
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            categoriaService.deleteById(nonExistentId);
        });
        assertEquals("Resource not found", exception.getMessage());
    }

}