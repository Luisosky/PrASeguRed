package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comentario")
public class ComentarioController {
    @Autowired
    private ComentarioService comentarioService;

    @PostMapping("/comentario")
    public ResponseEntity<String> registrarComentario(@RequestBody Comentario comentario) {
        comentarioService.registrarComentario(comentario);
        return ResponseEntity.status(201).body("Comentario registrado exitosamente");
    }

    @GetMapping
    public ResponseEntity<List<Comentario>> getAllComentarios() {
        List<Comentario> comentarios= comentarioService.getAllComentarios();
        return ResponseEntity.ok(comentarios);
    }

}
