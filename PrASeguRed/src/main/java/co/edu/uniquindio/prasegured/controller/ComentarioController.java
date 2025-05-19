package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    /**
     * Crear un comentario para un reporte específico
     */
    @PostMapping("/reportes/{reporteId}/comentarios")
    public ResponseEntity<ComentarioDTO> crearComentario(
            @PathVariable String reporteId,
            @RequestBody ComentarioRequest request) {

        // Usando el patrón de diseño con clase auxiliar para cuando necesites modificar un record
        ComentarioRequest requestConIdCorregido;

        // Si el ID del reporte en la solicitud es null o diferente, crear una nueva instancia
        if (request.idReporte() == null || !request.idReporte().equals(reporteId)) {
            requestConIdCorregido = new ComentarioRequest(
                    reporteId,
                    request.idUsuario(),
                    request.anonimo(),
                    request.nombre(),
                    request.descripcion(),
                    request.userImage()
            );
        } else {
            requestConIdCorregido = request;
        }

        ComentarioDTO comentarioCreado = comentarioService.guardarComentario(requestConIdCorregido);

        return ResponseEntity.created(URI.create("/api/reportes/" + reporteId + "/comentarios/" + comentarioCreado.id()))
                .body(comentarioCreado);
    }

    /**
     * Obtener todos los comentarios de un reporte
     */
    @GetMapping("/reportes/{reporteId}/comentarios")
    public ResponseEntity<List<ComentarioDTO>> obtenerComentariosPorReporte(
            @PathVariable String reporteId) {

        return ResponseEntity.ok(comentarioService.obtenerComentariosPorReporte(reporteId));
    }

    /**
     * Obtener un comentario específico de un reporte
     */
    @GetMapping("/reportes/{reporteId}/comentarios/{comentarioId}")
    public ResponseEntity<ComentarioDTO> obtenerComentario(
            @PathVariable String reporteId,
            @PathVariable String comentarioId) {

        // Implementar en el servicio un método para obtener un comentario específico
        ComentarioDTO comentario = comentarioService.obtenerComentario(comentarioId);

        // Verificar que el comentario pertenece al reporte
        if (!comentario.idReporte().equals(reporteId)) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comentario);
    }

    /**
     * Obtener todos los comentarios de un usuario
     */
    @GetMapping("/usuarios/{usuarioId}/comentarios")
    public ResponseEntity<List<ComentarioDTO>> obtenerComentariosPorUsuario(
            @PathVariable String usuarioId) {

        return ResponseEntity.ok(comentarioService.obtenerComentariosPorUsuario(usuarioId));
    }

    /**
     * Eliminar un comentario específico
     */
    @DeleteMapping("/comentarios/{comentarioId}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable String comentarioId) {
        comentarioService.eliminarComentario(comentarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Eliminar un comentario específico de un reporte
     */
    @DeleteMapping("/reportes/{reporteId}/comentarios/{comentarioId}")
    public ResponseEntity<Void> eliminarComentarioDeReporte(
            @PathVariable String reporteId,
            @PathVariable String comentarioId) {

        // Verificar que el comentario pertenece al reporte antes de eliminarlo
        ComentarioDTO comentario = comentarioService.obtenerComentario(comentarioId);

        if (comentario == null || !comentario.idReporte().equals(reporteId)) {
            return ResponseEntity.notFound().build();
        }

        comentarioService.eliminarComentario(comentarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Dar like a un comentario
     */
    @PostMapping("/reportes/{reporteId}/comentarios/{comentarioId}/like")
    public ResponseEntity<Void> darLike(
            @PathVariable String reporteId,
            @PathVariable String comentarioId,
            @RequestBody Map<String, String> request) {

        String userId = request.get("userId");
        comentarioService.darLike(comentarioId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Quitar like de un comentario
     */
    @DeleteMapping("/reportes/{reporteId}/comentarios/{comentarioId}/like/{userId}")
    public ResponseEntity<Void> quitarLike(
            @PathVariable String reporteId,
            @PathVariable String comentarioId,
            @PathVariable String userId) {

        comentarioService.quitarLike(comentarioId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Dar dislike a un comentario
     */
    @PostMapping("/reportes/{reporteId}/comentarios/{comentarioId}/dislike")
    public ResponseEntity<Void> darDislike(
            @PathVariable String reporteId,
            @PathVariable String comentarioId,
            @RequestBody Map<String, String> request) {

        String userId = request.get("userId");
        comentarioService.darDislike(comentarioId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Quitar dislike de un comentario
     */
    @DeleteMapping("/reportes/{reporteId}/comentarios/{comentarioId}/dislike/{userId}")
    public ResponseEntity<Void> quitarDislike(
            @PathVariable String reporteId,
            @PathVariable String comentarioId,
            @PathVariable String userId) {

        comentarioService.quitarDislike(comentarioId, userId);
        return ResponseEntity.noContent().build();
    }
}