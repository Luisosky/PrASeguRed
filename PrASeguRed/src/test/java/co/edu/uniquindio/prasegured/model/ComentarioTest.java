package co.edu.uniquindio.prasegured.model;

import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ComentarioTest {
    @Autowired
    private ComentarioRepository comentarioRepository;

    @BeforeEach
    void limpiarDB() {
        comentarioRepository.deleteAll();
    }

    @Test
    void guardarComentario_enMongoDB() {
        Comentario comentario = new Comentario();
        comentario.setId("c01");
        comentario.setIdReporte("rep01");
        comentario.setIdUsuario("user01");
        comentario.setAnonimo(false);
        comentario.setNombre("Juan");
        comentario.setDescripcion("Muy buen servicio");
        comentario.setFechaPublicacion(new Date());
        comentario.setEstado(EnumEstado.Espera);
        comentario.setCalificacion(4.5f);
        comentario.setNumeroCalificaciones(1);

        Comentario guardado = comentarioRepository.save(comentario);

        Optional<Comentario> buscado = comentarioRepository.findById("c01");
        assertTrue(buscado.isPresent());
        assertEquals("Muy buen servicio", buscado.get().getDescripcion());
    }
}
