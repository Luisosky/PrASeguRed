package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.exception.ResourceNotFoundException;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import co.edu.uniquindio.prasegured.mapper.ReporteMapper;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.repository.ImagenRepository;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ReporteServiceImple implements ReporteService {
    private static final Logger logger = LoggerFactory.getLogger(ReporteServiceImple.class);

    private final ReporteRepository reporteRepository;
    private final ImagenRepository imagenRepository;
    private final ReporteMapper reporteMapper;

    @Override
    @Transactional
    public ReporteDTO save(ReporteRequest reporte) {
        var newReporte = reporteMapper.parseOf(reporte);

        // Aseguramos que el ID del usuario no se pueda falsificar
        // ya que viene verificado desde el controlador con el token JWT

        // Si el ID del reporte ya existe, validamos
        if (reporte.id() != null && !reporte.id().isEmpty()) {
            validateReporteid(reporte.id());
        }

        // Aseguramos que tenga estado y fecha
        if (newReporte.getEstado() == null) {
            newReporte.setEstado(ESTADOREPORTE.Espera);
        }

        if (newReporte.getFechaPublicacion() == null) {
            newReporte.setFechaPublicacion(new Date());
        }

        if (newReporte.getFechaActualizacion() == null) {
            newReporte.setFechaActualizacion(new Date());
        }

        // Primero guardamos el reporte para obtener su ID
        Reporte savedReporte = reporteRepository.save(newReporte);

        // Procesamos las imágenes si hay
        if (reporte.imagenes() != null && !reporte.imagenes().isEmpty()) {
            List<Imagen> imagenesGuardadas = procesarImagenes(
                    reporte.imagenes(),
                    savedReporte.getId(),
                    reporte.idUsuario()
            );

            // Actualizamos el reporte con las imágenes guardadas
            savedReporte.setImagenes(imagenesGuardadas);
            savedReporte = reporteRepository.save(savedReporte);
        }

        return reporteMapper.toReporteDTO(savedReporte);
    }

    @Override
    @Transactional
    public ReporteDTO update(String id, ReporteRequest reporte) {
        // Buscar el reporte existente en la base de datos
        var existingReporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        // Validar si es necesario
        if (reporte.id() != null && !reporte.id().isEmpty() && !existingReporte.getId().equals(reporte.id())) {
            validateReporteid(reporte.id());
        }

        // Actualizar los campos del reporte existente
        existingReporte.setTitulo(reporte.titulo());
        existingReporte.setDescripcion(reporte.descripcion());
        existingReporte.setLocation(reporte.locations());
        existingReporte.setCategoria(reporte.categoria());
        existingReporte.setFechaActualizacion(new Date());

        // Procesar las imágenes
        if (reporte.imagenes() != null && !reporte.imagenes().isEmpty()) {
            // Si el reporte ya tenía imágenes, las eliminamos
            if (existingReporte.getImagenes() != null && !existingReporte.getImagenes().isEmpty()) {
                for (Imagen imagen : existingReporte.getImagenes()) {
                    try {
                        imagenRepository.deleteById(imagen.getId());
                    } catch (Exception e) {
                        logger.error("Error al eliminar imagen con ID: " + imagen.getId(), e);
                    }
                }
            }

            // Procesamos y guardamos las nuevas imágenes
            List<Imagen> imagenesGuardadas = procesarImagenes(
                    reporte.imagenes(),
                    existingReporte.getId(),
                    reporte.idUsuario()
            );

            existingReporte.setImagenes(imagenesGuardadas);
        }

        // Guardar el reporte actualizado
        var savedReporte = reporteRepository.save(existingReporte);

        // Convertir a DTO y devolver
        return reporteMapper.toReporteDTO(savedReporte);
    }

    @Override
    public List<ReporteDTO> findAll() {
        return reporteRepository.findAll()
                .stream()
                .map(reporteMapper::toReporteDTO)
                .toList();
    }

    @Override
    public ReporteDTO findById(String id) {
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return reporteMapper.toReporteDTO(storedReporte);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        // Marcamos el reporte como eliminado
        storedReporte.setEstado(ESTADOREPORTE.Eliminado);
        storedReporte.setFechaActualizacion(new Date());
        reporteRepository.save(storedReporte);

        // También marcamos las imágenes como eliminadas
        if (storedReporte.getImagenes() != null && !storedReporte.getImagenes().isEmpty()) {
            for (Imagen imagen : storedReporte.getImagenes()) {
                imagen.setEstado(ESTADOREPORTE.Eliminado);
                imagenRepository.save(imagen);
            }
        }
    }

    @Override
    public void reporteCompleto(String id) {
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        storedReporte.setEstado(ESTADOREPORTE.Completado);
        storedReporte.setFechaActualizacion(new Date());
        reporteRepository.save(storedReporte);
    }

    @Override
    public void estadoDenegado(String id) {
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        storedReporte.setEstado(ESTADOREPORTE.Denegado);
        storedReporte.setFechaActualizacion(new Date());
        reporteRepository.save(storedReporte);
    }

    private void validateReporteid(String id) {
        if (reporteRepository.findById(id).isPresent()) {
            throw new ValueConflictException("El reporte ya existe");
        }
    }

    /**
     * Procesa la lista de imágenes y las guarda en la base de datos
     * @param imagenes Lista de imágenes o datos de imágenes a procesar
     * @param reporteId ID del reporte al que pertenecen las imágenes
     * @param usuarioId ID del usuario que creó el reporte
     * @return Lista de imágenes procesadas y guardadas
     */
    @SuppressWarnings("unchecked")
    private List<Imagen> procesarImagenes(List<?> imagenes, String reporteId, String usuarioId) {
        List<Imagen> imagenesGuardadas = new ArrayList<>();

        if (imagenes != null && !imagenes.isEmpty()) {
            for (Object imagenInput : imagenes) {
                try {
                    // Crear una nueva imagen para guardar en la BD
                    Imagen imagen = new Imagen();
                    imagen.setId(UUID.randomUUID().toString());
                    imagen.setReporteId(reporteId);
                    imagen.setUsuarioId(usuarioId);
                    imagen.setEstado(ESTADOREPORTE.Espera);

                    // Determinar el tipo de objeto y procesarlo adecuadamente
                    if (imagenInput instanceof Imagen) {
                        // Si es una instancia de Imagen, copiar sus propiedades
                        Imagen imagenOriginal = (Imagen)imagenInput;

                        if (imagenOriginal.getNombre() != null) {
                            imagen.setNombre(imagenOriginal.getNombre());
                        } else {
                            imagen.setNombre("imagen_" + System.currentTimeMillis() + ".jpg");
                        }

                        if (imagenOriginal.getContent() != null && imagenOriginal.getContent().length > 0) {
                            imagen.setContent(imagenOriginal.getContent());
                        } else {
                            logger.warn("Imagen sin contenido");
                            continue;
                        }
                    }
                    else if (imagenInput instanceof Map) {
                        // Si es un Map (viene del frontend como JSON)
                        Map<String, Object> imagenMap = (Map<String, Object>)imagenInput;

                        // Obtener nombre si existe
                        if (imagenMap.containsKey("nombre")) {
                            imagen.setNombre(imagenMap.get("nombre").toString());
                        } else {
                            imagen.setNombre("imagen_" + System.currentTimeMillis() + ".jpg");
                        }

                        // Procesar contenido
                        if (imagenMap.containsKey("url")) {
                            String base64 = imagenMap.get("url").toString();
                            imagen.setContent(convertirBase64ABytes(base64));
                        } else if (imagenMap.containsKey("base64")) {
                            String base64 = imagenMap.get("base64").toString();
                            imagen.setContent(convertirBase64ABytes(base64));
                        } else {
                            logger.warn("No se encontró contenido de imagen en el objeto");
                            continue;
                        }
                    }
                    else if (imagenInput instanceof ImagenDTO) {
                        // Si es un ImagenDTO
                        ImagenDTO dto = (ImagenDTO)imagenInput;

                        if (dto.nombre() != null) {
                            imagen.setNombre(dto.nombre());
                        } else {
                            imagen.setNombre("imagen_" + System.currentTimeMillis() + ".jpg");
                        }

                        if (dto.content() != null && dto.content().length > 0) {
                            imagen.setContent(dto.content());
                        } else {
                            logger.warn("ImagenDTO sin contenido");
                            continue;
                        }
                    }
                    else {
                        logger.warn("Tipo de imagen no soportado: " + imagenInput.getClass().getName());
                        continue;
                    }

                    // Guardar la imagen
                    Imagen imagenGuardada = imagenRepository.save(imagen);
                    imagenesGuardadas.add(imagenGuardada);

                    logger.debug("Imagen guardada con ID: " + imagenGuardada.getId());
                } catch (Exception e) {
                    logger.error("Error al procesar imagen: " + e.getMessage(), e);
                }
            }
        }

        return imagenesGuardadas;
    }

    /**
     * Obtiene la URL de base64 de una imagen
     */
    private String obtenerUrlDeImagen(ImagenDTO imagenDTO) {
        // Esta es una implementación básica que podría extenderse según tus necesidades
        return ""; // Aquí deberías implementar la lógica para extraer la URL
    }

    /**
     * Convierte una cadena Base64 a un arreglo de bytes
     */
    private byte[] convertirBase64ABytes(String base64) {
        try {
            if (base64 == null || base64.isEmpty()) {
                throw new IllegalArgumentException("La cadena Base64 no puede ser nula o vacía");
            }

            // Eliminar el prefijo "data:image/tipo;base64," si existe
            String contenidoBase64 = base64;
            if (base64.contains(",")) {
                contenidoBase64 = base64.substring(base64.indexOf(",") + 1);
            }

            return Base64.getDecoder().decode(contenidoBase64);
        } catch (IllegalArgumentException e) {
            logger.error("Error al decodificar cadena Base64", e);
            throw e;
        }
    }
}