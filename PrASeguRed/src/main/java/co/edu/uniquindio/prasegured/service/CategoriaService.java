package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.CategoriaDTO;
import co.edu.uniquindio.prasegured.dto.CategoriaRequest;
import java.util.List;

public interface CategoriaService {
    CategoriaDTO save(CategoriaRequest categoria);
    CategoriaDTO update(String id, CategoriaRequest categoria);
    List<CategoriaDTO> findAll();
    CategoriaDTO findById(String id);
    void deleteById(String id);
}