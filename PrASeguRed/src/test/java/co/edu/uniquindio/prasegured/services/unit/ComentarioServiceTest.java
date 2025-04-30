package co.edu.uniquindio.prasegured.services.unit;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import co.edu.uniquindio.prasegured.mapper.ComentarioMapper;
import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import co.edu.uniquindio.prasegured.service.ComentarioServiceImple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private ComentarioMapper comentarioMapper;

    @InjectMocks
    private ComentarioServiceImple comentarioService;

    private Comentario comentario;
    private ComentarioDTO comentarioDTO;
    private ComentarioRequest comentarioRequest;

    @BeforeEach
    void setUp() {
        comentario = new Comentario();
        comentario.setId("com01");
        comentario.setIdReporte("rep01");
        comentario.setIdUsuario("user01");
        comentario.setAnonimo(false);
        comentario.setNombre("Comentario de prueba");
        comentario.setFechaPublicacion(new Date());
        comentario.setDescripcion("Este es un comentario de prueba.");
        comentario.setLikes(10);
        comentario.setDislikes(2);
        comentario.setEstado(ESTADOREPORTE.Espera);

        comentarioDTO = new ComentarioDTO(
                comentario.getId(),
                comentario.getIdReporte(),
                comentario.getIdUsuario(),
                comentario.getAnonimo(),
                comentario.getNombre(),
                comentario.getFechaPublicacion(),
                comentario.getDescripcion(),
                comentario.getLikes(),
                comentario.getDislikes(),
                comentario.getEstado()
        );

        comentarioRequest = new ComentarioRequest(
                comentario.getIdReporte(),
                comentario.getIdUsuario(),
                comentario.getAnonimo(),
                comentario.getNombre(),
                comentario.getDescripcion(),
                comentario.getLikes(),
                comentario.getDislikes()
        );
    }

    @Test
    void testGuardarComentario() {
        when(comentarioMapper.toComentario(comentarioRequest)).thenReturn(comentario);
        when(comentarioRepository.save(comentario)).thenReturn(comentario);
        when(comentarioMapper.toComentarioDTO(comentario)).thenReturn(comentarioDTO);

        ComentarioDTO result = comentarioService.guardarComentario(comentarioRequest);

        assertNotNull(result);
        assertEquals(comentarioDTO.id(), result.id());
        assertEquals(comentarioDTO.descripcion(), result.descripcion());

        verify(comentarioMapper).toComentario(comentarioRequest);
        verify(comentarioRepository).save(comentario);
        verify(comentarioMapper).toComentarioDTO(comentario);
    }

    @Test
    void testObtenerComentariosPorReporte() {
        when(comentarioRepository.findAllByIdReporte(comentario.getIdReporte())).thenReturn(List.of(comentario));
        when(comentarioMapper.toComentarioDTO(comentario)).thenReturn(comentarioDTO);

        List<ComentarioDTO> result = comentarioService.obtenerComentariosPorReporte(comentario.getIdReporte());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(comentarioDTO.id(), result.get(0).id());

        verify(comentarioRepository).findAllByIdReporte(comentario.getIdReporte());
        verify(comentarioMapper).toComentarioDTO(comentario);
    }

    @Test
    void testObtenerComentariosPorUsuario() {
        when(comentarioRepository.findAllByIdUsuario(comentario.getIdUsuario())).thenReturn(List.of(comentario));
        when(comentarioMapper.toComentarioDTO(comentario)).thenReturn(comentarioDTO);

        List<ComentarioDTO> result = comentarioService.obtenerComentariosPorUsuario(comentario.getIdUsuario());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(comentarioDTO.id(), result.get(0).id());

        verify(comentarioRepository).findAllByIdUsuario(comentario.getIdUsuario());
        verify(comentarioMapper).toComentarioDTO(comentario);
    }

    @Test
    void testEliminarComentario() {
        when(comentarioRepository.findById(comentario.getId())).thenReturn(Optional.of(comentario));

        comentarioService.eliminarComentario(comentario.getId());

        assertEquals(ESTADOREPORTE.Eliminado, comentario.getEstado());

        verify(comentarioRepository).findById(comentario.getId());
        verify(comentarioRepository).save(comentario);
    }

    @Test
    void testEliminarComentarioNoEncontrado() {
        when(comentarioRepository.findById(comentario.getId())).thenReturn(Optional.empty());

        assertThrows(ValueConflictException.class, () -> comentarioService.eliminarComentario(comentario.getId()));

        verify(comentarioRepository).findById(comentario.getId());
        verify(comentarioRepository, never()).save(any(Comentario.class));
    }

    @Test
    void testDenegarComentario() {
        when(comentarioRepository.findById(comentario.getId())).thenReturn(Optional.of(comentario));

        comentarioService.denegarComentario(comentario.getId());

        assertEquals(ESTADOREPORTE.Denegado, comentario.getEstado());

        verify(comentarioRepository).findById(comentario.getId());
        verify(comentarioRepository).save(comentario);
    }
}