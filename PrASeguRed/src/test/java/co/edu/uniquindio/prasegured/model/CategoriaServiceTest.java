package co.edu.uniquindio.prasegured.model;

import co.edu.uniquindio.prasegured.dto.CategoriaDTO;
import co.edu.uniquindio.prasegured.repository.CategoriaRepository;
import co.edu.uniquindio.prasegured.service.CategoriaService;
import co.edu.uniquindio.prasegured.service.CategoriaServiceImple;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

public class CategoriaServiceTest {

    private final CategoriaService categoriaService;
    private final CategoriaRepository categoriaRepository;

    @BeforeEach
    void setUp() {
        categoriaRepository = mock(CategoriaRepository.class);
        categoriaService = new CategoriaServiceImple(categoriaRepository);
    }

}
