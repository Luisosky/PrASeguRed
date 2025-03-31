package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria registrarCategoria(Categoria categoria) {

        return categoriaRepository.save(categoria);
    }

    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }
}
