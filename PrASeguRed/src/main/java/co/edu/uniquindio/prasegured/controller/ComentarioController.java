package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.service.ComentarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService comentarioService;

    @PostMapping("/guardar")
    public ResponseEntity<ComentarioDTO> guardarComentario(@RequestBody ComentarioRequest request) {
        ComentarioDTO dto = comentarioService.guardarComentario(request);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/por-reporte/{idReporte}")
    public ResponseEntity<List<ComentarioDTO>> comentariosPorReporte(@PathVariable String idReporte) {
        return ResponseEntity.ok(comentarioService.obtenerComentariosPorReporte(idReporte));
    }

    @GetMapping("/por-usuario/{idUsuario}")
    public ResponseEntity<List<ComentarioDTO>> comentariosPorUsuario(@PathVariable String idUsuario) {
        return ResponseEntity.ok(comentarioService.obtenerComentariosPorUsuario(idUsuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable String id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/denegar/{id}")
    public ResponseEntity<Void> denegarComentario(@PathVariable String id) {
        comentarioService.denegarComentario(id);
        return ResponseEntity.noContent().build();
    }
}

