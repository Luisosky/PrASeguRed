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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImagenServicesImple implements ImagenService {

    private static final Logger logger = LoggerFactory.getLogger(ImagenServicesImple.class);

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
        logger.info("Imagen guardada con ID: {} y tamaño: {} bytes", saved.getId(), saved.getContent().length);
        return imagenMapper.toImagenDTO(saved);
    }

    @Override
    public void deleteImagen(String id) throws IOException {
        var storedImagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ValueConflictException("No existe una imagen con el id: " + id));
        storedImagen.setEstado(ESTADOREPORTE.Eliminado);
        imagenRepository.save(storedImagen);
        logger.info("Imagen marcada como eliminada: {}", id);
    }

    @Override
    public ImagenDTO updateImagen(String id, MultipartFile file) throws IOException {
        Imagen imagenExistente = imagenRepository.findById(id)
                .orElseThrow(() -> new ValueConflictException("No existe una imagen con el id: " + id));
        imagenExistente.setNombre(file.getOriginalFilename());
        imagenExistente.setContent(file.getBytes());
        Imagen actualizada = imagenRepository.save(imagenExistente);
        logger.info("Imagen actualizada: {}", id);
        return imagenMapper.toImagenDTO(actualizada);
    }

    @Override
    public void estadoDenegado(String id) throws IOException {
        var storedImagen = imagenRepository.findById(id)
                .orElseThrow(() -> new ValueConflictException("No existe una imagen con el id: " + id));
        storedImagen.setEstado(ESTADOREPORTE.Denegado);
        imagenRepository.save(storedImagen);
        logger.info("Imagen marcada como denegada: {}", id);
    }

    @Override
    public List<ImagenDTO> findAllByReporteId(String reporteId) {
        List<Imagen> imagenes = imagenRepository.findByReporteId(reporteId);
        logger.info("Consultadas {} imágenes para el reporte {}", imagenes.size(), reporteId);
        return imagenes.stream()
                .map(imagenMapper::toImagenDTO)
                .toList();
    }

    @Override
    public List<ImagenDTO> findAllByUsuarioId(String usuarioId) {
        List<Imagen> imagenes = imagenRepository.findByUsuarioId(usuarioId);
        logger.info("Consultadas {} imágenes para el usuario {}", imagenes.size(), usuarioId);
        return imagenes.stream()
                .map(imagenMapper::toImagenDTO)
                .toList();
    }

    private void validarImagenId(String id) {
        var imagen = imagenRepository.findById(id);
        if (imagen.isPresent()) {
            throw new ValueConflictException("Ya existe una imagen con el id: " + id);
        }
    }

    /**
     * Método para convertir Base64 a bytes
     */
    public static byte[] convertBase64ToBytes(String base64) {
        try {
            if (base64 == null || base64.trim().isEmpty()) {
                return null;
            }

            // Eliminar prefijo data:image si existe
            String cleanBase64 = base64;
            if (base64.contains(",")) {
                cleanBase64 = base64.substring(base64.indexOf(",") + 1);
            }

            return Base64.getDecoder().decode(cleanBase64);
        } catch (IllegalArgumentException e) {
            LoggerFactory.getLogger(ImagenServicesImple.class).error("Error decodificando Base64: {}", e.getMessage());
            return null;
        }
    }
}