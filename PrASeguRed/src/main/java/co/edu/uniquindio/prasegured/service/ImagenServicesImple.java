package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import co.edu.uniquindio.prasegured.mapper.ImagenMapper;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.repository.ImagenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImagenServicesImple implements ImagenService {

    private final ImagenRepository imagenRepository;
    private final ImagenMapper imagenMapper;

    @Override
    public ImagenDTO saveImagen(MultipartFile file, String reporteId, String usuarioId) throws IOException {
        Imagen imagen = new Imagen();
        imagen.setId(UUID.randomUUID().toString());
        imagen.setNombre(file.getOriginalFilename());
        imagen.setContent(file.getBytes());
        imagen.setReporteId(reporteId);
        imagen.setUsuarioId(usuarioId);
        imagen.setEstado(ESTADOREPORTE.Espera);

        Imagen saved = imagenRepository.save(imagen);
        return imagenMapper.toImagenDTO(saved);
    }


    @Override
    public void deleteImagen(String id) throws IOException {
        var StoredImagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ValueConflictException("No existe una imagen con el id: " + id));
        StoredImagen.setEstado(ESTADOREPORTE.Eliminado);
        imagenRepository.save(StoredImagen);
    }

    @Override
    public ImagenDTO updateImagen(String id, MultipartFile file) throws IOException {
        Imagen imagenExistente = imagenRepository.findById(id)
                .orElseThrow(() -> new ValueConflictException("No existe una imagen con el id: " + id));
        imagenExistente.setNombre(file.getOriginalFilename());
        imagenExistente.setContent(file.getBytes());
        // Puedes modificar también estado si lo deseas
        Imagen actualizada = imagenRepository.save(imagenExistente);
        return imagenMapper.toImagenDTO(actualizada);
    }


    @Override
    public void estadoDenegado(String id) throws IOException {
        var StoredImagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ValueConflictException("No existe una imagen con el id: " + id));
        StoredImagen.setEstado(ESTADOREPORTE.Denegado);
        imagenRepository.save(StoredImagen);
    }

    @Override
    public List<ImagenDTO> findAllByReporteId(String reporteId) {
        List<Imagen> imagenes = imagenRepository.findByReporteId(reporteId);
        return imagenes.stream()
                .map(imagenMapper::toImagenDTO)
                .toList(); // Convierte las entidades a DTOs
    }

    @Override
    public List<ImagenDTO> findAllByUsuarioId(String usuarioId) {
        List<Imagen> imagenes = imagenRepository.findByUsuarioId(usuarioId);
        return imagenes.stream()
                .map(imagenMapper::toImagenDTO)
                .toList(); // Convierte las entidades a DTOs
    }

    private void validarImagenId(String id) {
        var imagen = imagenRepository.findById(id);
        if (imagen.isPresent()) {
            throw new ValueConflictException("Ya existe una imagen con el id: " + id);
        }
    }
}
