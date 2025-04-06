package co.edu.uniquindio.prasegured.model;

import co.edu.uniquindio.prasegured.repository.ImagenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImagenServiceTest {
    @Autowired
    private ImagenRepository imagenRepository;

    @BeforeEach
    void limpiarDB() {
        imagenRepository.deleteAll();
    }

    @Test
    void guardarImagen_enMongoDB() {
        Imagen imagen = new Imagen();
        imagen.setId("img01");
        imagen.setReporteId("rep01");
        imagen.setUsuarioId("user01");
        imagen.setNombre("prueba.jpg");
        imagen.setEstado(EnumEstado.Espera);
        imagen.setContent("contenido de prueba".getBytes());

        Imagen guardada = imagenRepository.save(imagen);

        Optional<Imagen> buscada = imagenRepository.findById("img01");
        assertTrue(buscada.isPresent());
        assertEquals("prueba.jpg", buscada.get().getNombre());
    }
}
