package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @PostMapping("/categoria")
    public ResponseEntity<String> registrarCategoria(@RequestBody Categoria categoria){
        categoriaService.registrarCategoria(categoria);
        return ResponseEntity.status(201).body("Registro exitoso");
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.getAllCategorias();
        return ResponseEntity.ok(categorias);
    }

}
