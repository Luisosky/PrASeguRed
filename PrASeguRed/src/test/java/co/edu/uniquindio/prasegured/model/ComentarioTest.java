package co.edu.uniquindio.prasegured.model;

import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.Date;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Profile("test")
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
        comentario.setEstado(ESTADOREPORTE.Espera);
        comentario.setLikes(4);
        comentario.setDislikes(1);

        Comentario guardado = comentarioRepository.save(comentario);

        Optional<Comentario> buscado = comentarioRepository.findById("c01");
        assertTrue(buscado.isPresent());
        assertEquals("Muy buen servicio", buscado.get().getDescripcion());
    }

    @Test
    void eliminarComentario_enMongoDB() {
        // Primero se crea un comentario a eliminar
        Comentario comentario = new Comentario();
        comentario.setId("c02");
        comentario.setIdReporte("rep02");
        comentario.setIdUsuario("user02");
        comentario.setAnonimo(true);
        comentario.setNombre("Anónimo");
        comentario.setDescripcion("No me gustó el trato.");
        comentario.setFechaPublicacion(new Date());
        comentario.setEstado(ESTADOREPORTE.Espera);
        comentario.setLikes(0);
        comentario.setDislikes(5);
        comentarioRepository.save(comentario);
        Optional<Comentario> buscado = comentarioRepository.findById("c02");
        assertTrue(buscado.isPresent());
        comentarioRepository.delete(buscado.get());
        Optional<Comentario> eliminado = comentarioRepository.findById("c02");
        assertTrue(eliminado.isEmpty());
    }


}
