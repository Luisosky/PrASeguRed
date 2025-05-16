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
        logger.info("Guardando nuevo reporte: {}", reporte.titulo());
        var newReporte = reporteMapper.parseOf(reporte);

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
        logger.info("Reporte guardado con ID: {}", savedReporte.getId());

        // Procesamos las imágenes si hay
        if (reporte.imagenes() != null && !reporte.imagenes().isEmpty()) {
            logger.info("Procesando {} imágenes para el reporte", reporte.imagenes().size());
            List<Imagen> imagenesGuardadas = procesarImagenes(
                    reporte.imagenes(),
                    savedReporte.getId(),
                    reporte.idUsuario()
            );

            if (!imagenesGuardadas.isEmpty()) {
                // Actualizamos el reporte con las imágenes guardadas
                savedReporte.setImagenes(imagenesGuardadas);
                savedReporte = reporteRepository.save(savedReporte);
                logger.info("Reporte actualizado con {} imágenes", imagenesGuardadas.size());
            } else {
                logger.warn("No se pudieron guardar imágenes para el reporte");
            }
        } else {
            logger.info("El reporte no incluye imágenes");
        }

        return reporteMapper.toReporteDTO(savedReporte);
    }

    @Override
    @Transactional
    public ReporteDTO update(String id, ReporteRequest reporte) {
        logger.info("Actualizando reporte con ID: {}", id);

        // Buscar el reporte existente en la base de datos
        var existingReporte = reporteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Reporte no encontrado con ID: {}", id);
                    return new ResourceNotFoundException();
                });

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
            logger.info("Actualizando imágenes del reporte, recibidas {} nuevas imágenes", reporte.imagenes().size());

            // Si el reporte ya tenía imágenes, las eliminamos
            if (existingReporte.getImagenes() != null && !existingReporte.getImagenes().isEmpty()) {
                logger.info("Eliminando {} imágenes anteriores", existingReporte.getImagenes().size());
                for (Imagen imagen : existingReporte.getImagenes()) {
                    try {
                        imagenRepository.deleteById(imagen.getId());
                        logger.debug("Imagen eliminada con ID: {}", imagen.getId());
                    } catch (Exception e) {
                        logger.error("Error al eliminar imagen con ID: {}", imagen.getId(), e);
                    }
                }
            }

            // Procesamos y guardamos las nuevas imágenes
            List<Imagen> imagenesGuardadas = procesarImagenes(
                    reporte.imagenes(),
                    existingReporte.getId(),
                    reporte.idUsuario()
            );

            if (!imagenesGuardadas.isEmpty()) {
                existingReporte.setImagenes(imagenesGuardadas);
                logger.info("Se guardaron {} nuevas imágenes", imagenesGuardadas.size());
            } else {
                logger.warn("No se pudieron guardar las nuevas imágenes");
            }
        } else {
            logger.info("No se enviaron nuevas imágenes para actualizar");
        }

        // Guardar el reporte actualizado
        var savedReporte = reporteRepository.save(existingReporte);
        logger.info("Reporte actualizado correctamente");

        // Convertir a DTO y devolver
        return reporteMapper.toReporteDTO(savedReporte);
    }

    @Override
    public List<ReporteDTO> findAll() {
        logger.info("Obteniendo todos los reportes");
        return reporteRepository.findAll()
                .stream()
                .map(reporteMapper::toReporteDTO)
                .toList();
    }

    @Override
    public ReporteDTO findById(String id) {
        logger.info("Buscando reporte por ID: {}", id);
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Reporte no encontrado con ID: {}", id);
                    return new ResourceNotFoundException();
                });
        return reporteMapper.toReporteDTO(storedReporte);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        logger.info("Marcando como eliminado el reporte con ID: {}", id);
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());

        // Marcamos el reporte como eliminado
        storedReporte.setEstado(ESTADOREPORTE.Eliminado);
        storedReporte.setFechaActualizacion(new Date());
        reporteRepository.save(storedReporte);

        // También marcamos las imágenes como eliminadas
        if (storedReporte.getImagenes() != null && !storedReporte.getImagenes().isEmpty()) {
            logger.info("Marcando como eliminadas {} imágenes asociadas", storedReporte.getImagenes().size());
            for (Imagen imagen : storedReporte.getImagenes()) {
                imagen.setEstado(ESTADOREPORTE.Eliminado);
                imagenRepository.save(imagen);
            }
        }

        logger.info("Reporte y sus imágenes marcados como eliminados correctamente");
    }

    @Override
    public void reporteCompleto(String id) {
        logger.info("Marcando reporte como completado: {}", id);
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
        storedReporte.setEstado(ESTADOREPORTE.Completado);
        storedReporte.setFechaActualizacion(new Date());
        reporteRepository.save(storedReporte);
        logger.info("Reporte marcado como completado correctamente");
    }

    @Override
    public void estadoDenegado(String id) {
        logger.info("Marcando reporte como denegado: {}", id);
        var storedReporte = reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException());
        storedReporte.setEstado(ESTADOREPORTE.Denegado);
        storedReporte.setFechaActualizacion(new Date());
        reporteRepository.save(storedReporte);
        logger.info("Reporte marcado como denegado correctamente");
    }

    private void validateReporteid(String id) {
        logger.debug("Validando si existe reporte con ID: {}", id);
        if (reporteRepository.findById(id).isPresent()) {
            logger.warn("Intento de crear reporte con ID existente: {}", id);
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
    private List<Imagen> procesarImagenes(List<?> imagenes, String reporteId, String usuarioId) {
        List<Imagen> imagenesGuardadas = new ArrayList<>();
        logger.info("Procesando lista de {} imágenes para el reporte {}", imagenes.size(), reporteId);

        for (int i = 0; i < imagenes.size(); i++) {
            try {
                Object imagenInput = imagenes.get(i);
                logger.debug("Procesando imagen {} de tipo {}", i+1, imagenInput.getClass().getName());

                // Crear una nueva imagen para guardar en la BD
                Imagen imagen = new Imagen();
                imagen.setId(UUID.randomUUID().toString());
                imagen.setReporteId(reporteId);
                imagen.setUsuarioId(usuarioId);
                imagen.setEstado(ESTADOREPORTE.Espera);

                // Por defecto asignar un nombre basado en índice y timestamp
                imagen.setNombre("imagen_" + (i+1) + "_" + System.currentTimeMillis() + ".jpg");

                // Determinar el tipo de objeto y procesarlo adecuadamente
                if (imagenInput instanceof Imagen) {
                    procesarObjetoImagen((Imagen)imagenInput, imagen);
                }
                else if (imagenInput instanceof Map) {
                    procesarMapaImagen((Map<String, Object>)imagenInput, imagen);
                }
                else if (imagenInput instanceof ImagenDTO) {
                    procesarImagenDTO((ImagenDTO)imagenInput, imagen);
                }
                else {
                    logger.warn("Tipo de imagen no soportado: {}", imagenInput.getClass().getName());
                    continue;
                }

                // Verificar que la imagen tiene contenido antes de guardar
                if (imagen.getContent() == null || imagen.getContent().length == 0) {
                    logger.warn("Contenido de imagen vacío, no se guarda");
                    continue;
                }

                // Guardar la imagen
                Imagen imagenGuardada = imagenRepository.save(imagen);
                imagenesGuardadas.add(imagenGuardada);
                logger.info("Imagen guardada exitosamente con ID: {}", imagenGuardada.getId());

            } catch (Exception e) {
                logger.error("Error al procesar imagen: {}", e.getMessage(), e);
            }
        }

        logger.info("Proceso finalizado: {} imágenes guardadas de {} recibidas", imagenesGuardadas.size(), imagenes.size());
        return imagenesGuardadas;
    }

    /**
     * Procesa un objeto de tipo Imagen
     */
    private void procesarObjetoImagen(Imagen imagenOriginal, Imagen imagen) {
        logger.debug("Procesando objeto Imagen");

        if (imagenOriginal.getNombre() != null) {
            imagen.setNombre(imagenOriginal.getNombre());
        }

        if (imagenOriginal.getContent() != null && imagenOriginal.getContent().length > 0) {
            imagen.setContent(imagenOriginal.getContent());
            logger.debug("Contenido copiado, longitud: {} bytes", imagenOriginal.getContent().length);
        } else {
            logger.warn("Objeto Imagen sin contenido");
        }
    }

    /**
     * Procesa un mapa que representa una imagen
     */
    @SuppressWarnings("unchecked")
    private void procesarMapaImagen(Map<String, Object> imagenMap, Imagen imagen) {
        logger.debug("Procesando Map<String, Object>, keys: {}", imagenMap.keySet());

        // Obtener nombre si existe
        if (imagenMap.containsKey("nombre")) {
            imagen.setNombre(imagenMap.get("nombre").toString());
            logger.debug("Nombre obtenido: {}", imagen.getNombre());
        }

        // Procesar contenido
        if (imagenMap.containsKey("url")) {
            String base64 = imagenMap.get("url").toString();
            logger.debug("Encontrado campo 'url' con contenido base64 (longitud: {} caracteres)", base64.length());
            imagen.setContent(convertirBase64ABytes(base64));
        } else if (imagenMap.containsKey("base64")) {
            String base64 = imagenMap.get("base64").toString();
            logger.debug("Encontrado campo 'base64' con contenido (longitud: {} caracteres)", base64.length());
            imagen.setContent(convertirBase64ABytes(base64));
        } else {
            logger.warn("No se encontró contenido de imagen (url o base64) en el objeto Map");
        }
    }

    /**
     * Procesa un objeto ImagenDTO
     */
    private void procesarImagenDTO(ImagenDTO dto, Imagen imagen) {
        logger.debug("Procesando ImagenDTO");

        if (dto.nombre() != null) {
            imagen.setNombre(dto.nombre());
            logger.debug("Nombre obtenido: {}", imagen.getNombre());
        }

        if (dto.content() != null && dto.content().length > 0) {
            imagen.setContent(dto.content());
            logger.debug("Contenido copiado, longitud: {} bytes", dto.content().length);
        } else {
            logger.warn("ImagenDTO sin contenido");
        }
    }

    /**
     * Convierte una cadena Base64 a un arreglo de bytes
     */
    private byte[] convertirBase64ABytes(String base64) {
        try {
            if (base64 == null || base64.isEmpty()) {
                logger.warn("La cadena Base64 es nula o vacía");
                throw new IllegalArgumentException("La cadena Base64 no puede ser nula o vacía");
            }

            // Eliminar el prefijo "data:image/tipo;base64," si existe
            String contenidoBase64 = base64;
            if (base64.contains(",")) {
                contenidoBase64 = base64.substring(base64.indexOf(",") + 1);
                logger.debug("Se eliminó el prefijo data:image del Base64");
            }

            byte[] bytes = Base64.getDecoder().decode(contenidoBase64);
            logger.debug("Base64 decodificado correctamente, tamaño: {} bytes", bytes.length);
            return bytes;
        } catch (IllegalArgumentException e) {
            logger.error("Error al decodificar cadena Base64: {}", e.getMessage());
            // Retornamos un array vacío en lugar de lanzar una excepción para no interrumpir el proceso
            return new byte[0];
        }
    }
}