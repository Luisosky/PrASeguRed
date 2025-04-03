package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.CategoriaDTO;
import co.edu.uniquindio.prasegured.dto.CategoriaRequest;
import co.edu.uniquindio.prasegured.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    private final CategoriaService categoriaServices;

    @Autowired
    public CategoriaController(CategoriaService categoriaServices) {
        this.categoriaServices = categoriaServices;
    }

    @PostMapping
    public CategoriaDTO create(@Valid CategoriaRequest categoria) {
        return categoriaServices.save(categoria);
    }

    @GetMapping
    public List<CategoriaDTO> getAll() {
        return categoriaServices.findAll();
    }

    @PutMapping("/{id}")
    public CategoriaDTO update(@PathVariable("id") String id, @Valid CategoriaRequest categoria) {
        return categoriaServices.update(id, categoria);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        categoriaServices.deleteById(id);
    }

    @GetMapping("/{id}")
    public CategoriaDTO findById(@PathVariable("id") String id) {
        return categoriaServices.findById(id);
    }
}