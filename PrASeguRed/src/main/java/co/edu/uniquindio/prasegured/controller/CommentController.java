package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.CommentRequest;
import co.edu.uniquindio.prasegured.model.Comment;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Comments", description = "API for comment management")
public class CommentController {

    @PostMapping("/comentarios")
    public ResponseEntity<Map<String, String>> createComment(@RequestBody CommentRequest request) {
        return new ResponseEntity<>(
                Map.of("message", "Comentario creado exitosamente."),
                HttpStatus.CREATED);
    }

    @PatchMapping("/comentario/{id}")
    public ResponseEntity<Map<String, String>> updateComment(
            @PathVariable String id,
            @RequestBody CommentRequest request) {
        return ResponseEntity.ok(Map.of("message", "Comentario actualizado."));
    }

    @DeleteMapping("/comentario/{id}")
    public ResponseEntity<Map<String, String>> deleteComment(@PathVariable String id) {

        return ResponseEntity.ok(Map.of("message", "Comentario eliminado correctamente."));
    }

    @GetMapping("/comentario/usuario/{usuarioId}")
    public ResponseEntity<List<Comment>> getCommentsByUser(@PathVariable String usuarioId) {

        List<Comment> comments = new ArrayList<>();
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/comentario")
    public ResponseEntity<List<Comment>> getAllComments() {

        List<Comment> comments = new ArrayList<>();
        return ResponseEntity.ok(comments);
    }
}