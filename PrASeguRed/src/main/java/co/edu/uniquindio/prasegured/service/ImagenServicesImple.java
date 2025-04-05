package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.dto.ImagenRequest;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import co.edu.uniquindio.prasegured.mapper.ImagenMapper;
import co.edu.uniquindio.prasegured.model.EnumEstado;
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
        imagen.setEstado(EnumEstado.Espera);

        Imagen saved = imagenRepository.save(imagen);
        return imagenMapper.toImagenDTO(saved);
    }


    @Override
    public void deleteImagen(String id) throws IOException {
        var StoredImagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ValueConflictException("No existe una imagen con el id: " + id));
        StoredImagen.setEstado(EnumEstado.Eliminado);
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
        StoredImagen.setEstado(EnumEstado.Denegado);
        imagenRepository.save(StoredImagen);
    }

    @Override
    public List<Imagen> findAllByReporteId(String reporteId) {
        return imagenRepository.findByReporteId(reporteId)
                .map(List::of)
                .orElse(List.of());
    }

    @Override
    public List<Imagen> findAllByUsuarioId(String usuarioId) {
        return imagenRepository.findByUsuarioId(usuarioId)
                .map(List::of)
                .orElse(List.of());
    }

    private void validarImagenId(String id) {
        var imagen = imagenRepository.findById(id);
        if (imagen.isPresent()) {
            throw new ValueConflictException("Ya existe una imagen con el id: " + id);
        }
    }
}
