package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.model.Imagen;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ImagenService {
    ImagenDTO saveImagen(MultipartFile file, String reporteId, String usuarioId) throws IOException;

    void deleteImagen(String id) throws IOException;

    ImagenDTO updateImagen(String id, MultipartFile file) throws IOException;

    void estadoDenegado(String id) throws IOException;

    List<ImagenDTO> findAllByReporteId(String reporteId);

    List<ImagenDTO> findAllByUsuarioId(String usuarioId);
}