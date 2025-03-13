package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Operation(
            summary = "Crear comentario",
            description = "Crea un nuevo comentario en un reporte",
            operationId = "crearComentario"
    )
    @ApiResponse(responseCode = "201", description = "Comentario creado exitosamente.")
    @PostMapping
    public ResponseEntity<Void> crearComentario(@RequestBody ComentarioDTO comentario) {
        // Implementación para crear un comentario
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Actualizar comentario",
            description = "Modifica un comentario existente",
            operationId = "actualizarComentario"
    )
    @ApiResponse(responseCode = "200", description = "Comentario actualizado.")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> actualizarComentario(
            @PathVariable String id,
            @RequestBody ComentarioDTO comentario) {
        // Implementación para actualizar un comentario
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Eliminar comentario",
            description = "Elimina un comentario",
            operationId = "eliminarComentario"
    )
    @ApiResponse(responseCode = "200", description = "Comentario eliminado correctamente.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable String id) {
        // Implementación para eliminar un comentario
        return ResponseEntity.ok().build();
    }
}