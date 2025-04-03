package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.CategoriaDTO;
import co.edu.uniquindio.prasegured.dto.CategoriaRequest;
import co.edu.uniquindio.prasegured.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public CategoriaDTO create(@Valid @RequestBody CategoriaRequest categoria) {
        return categoriaService.save(categoria);
    }

    @GetMapping
    public List<CategoriaDTO> getAll() {
        return categoriaService.findAll();
    }

    @PutMapping("/{id}")
    public CategoriaDTO update(@PathVariable("id") String id, @Valid @RequestBody CategoriaRequest categoria) {
        return categoriaService.update(id, categoria);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        categoriaService.deleteById(id);
    }

    @GetMapping("/{id}")
    public CategoriaDTO findById(@PathVariable("id") String id) {
        return categoriaService.findById(id);
    }
}