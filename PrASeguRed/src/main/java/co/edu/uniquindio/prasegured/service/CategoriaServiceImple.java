package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.CategoriaDTO;
import co.edu.uniquindio.prasegured.dto.CategoriaRequest;
import co.edu.uniquindio.prasegured.mapper.CategoriaMapper;
import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.repository.CategoriaRepository;
import co.edu.uniquindio.prasegured.exception.ResourceNotFoundException;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImple implements CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    public CategoriaDTO save(CategoriaRequest categoria) {
        var newCategoria = categoriaMapper.parseOf(categoria);
        validateCategoriaName(categoria.getName());
        return categoriaMapper.toCategoriaDTO(
                categoriaRepository.save(newCategoria)
        );
    }

    @Override
    public CategoriaDTO update(String id, CategoriaRequest categoria) {
        var updatedCategoria = findCategoriaById(id);
        updatedCategoria.setName(categoria.getName());
        if (!updatedCategoria.getName().equals(categoria.getName())) {
            validateCategoriaName(categoria.getName());
        }
        updatedCategoria.setDescripcion(categoria.getDescripcion());
        return categoriaMapper.toCategoriaDTO(
                categoriaRepository.save(updatedCategoria)
        );
    }

    @Override
    public List<CategoriaDTO> findAll() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toCategoriaDTO)
                .toList();
    }

    @Override
    public CategoriaDTO findById(String id) {
        return categoriaMapper.toCategoriaDTO(findCategoriaById(id));
    }

    @Override
    public void deleteById(String id) {
        var categoriaStored = findCategoriaById(id);
        categoriaStored.setStatus("DELETED");
        categoriaRepository.save(categoriaStored);
    }

    private Categoria findCategoriaById(String id) {
        var storedCategoria = categoriaRepository.findById(id);
        return storedCategoria.orElseThrow(ResourceNotFoundException::new);
    }

    private void validateCategoriaName(String categoriaName) {
        var categoria = categoriaRepository.findByName(categoriaName);
        if (categoria.isPresent()) {
            throw new ValueConflictException("Categoria name already exists");
        }
    }
}