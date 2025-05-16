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

        // Creamos una nueva instancia de Reporte en lugar de usar parseOf
        Reporte newReporte = crearReporteDesdeRequest(reporte);

        // Si el ID del reporte ya existe, validamos
        if (reporte.id() != null && !reporte.id().isEmpty()) {
            validateReporteid(reporte.id());
        }

        // Aseguramos que tenga estado y fecha
        newReporte.setEstado(ESTADOREPORTE.Espera);
        newReporte.setFechaPublicacion(new Date());
        newReporte.setFechaActualizacion(new Date());

        // Primero guardamos el reporte para obtener su ID
        Reporte savedReporte = reporteRepository.save(newReporte);
        logger.info("Reporte guardado con ID: {}", savedReporte.getId());

        // Procesamos las imágenes si hay
        if (reporte.imagenes() != null && !reporte.imagenes().isEmpty()) {
            logger.info("Procesando {} imágenes para el reporte", reporte.imagenes().size());
            List<Imagen> imagenesGuardadas = procesarImagenesMejorado(
                    reporte.imagenes(),
                    savedReporte.getId(),
                    reporte.idUsuario()
            );

            if (!imagenesGuardadas.isEmpty()) {
                // Actualizamos el reporte con las imágenes guardadas
                savedReporte.setImagenes(imagenesGuardadas);
                savedReporte = reporteRepository.save(savedReporte);
                logger.info("✅ Reporte actualizado con {} imágenes", imagenesGuardadas.size());
            } else {
                logger.warn("⚠️ No se pudieron guardar imágenes para el reporte");
            }
        } else {
            logger.info("El reporte no incluye imágenes");
        }

        // Volver a cargar el reporte para asegurarnos de tener los datos más recientes
        Reporte reporteFinal = reporteRepository.findById(savedReporte.getId())
                .orElse(savedReporte);

        return reporteMapper.toReporteDTO(reporteFinal);
    }

    // Método para crear un objeto Reporte desde un ReporteRequest
    private Reporte crearReporteDesdeRequest(ReporteRequest request) {
        Reporte reporte = new Reporte();

        // Si hay un ID, lo establecemos
        if (request.id() != null && !request.id().isEmpty()) {
            reporte.setId(request.id());
        }

        reporte.setTitulo(request.titulo());
        reporte.setDescripcion(request.descripcion());
        reporte.setIdUsuario(request.idUsuario());
        reporte.setCategoria(request.categoria());
        reporte.setLocation(request.locations());

        return reporte;
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
            List<Imagen> imagenesGuardadas = procesarImagenesMejorado(
                    reporte.imagenes(),
                    existingReporte.getId(),
                    reporte.idUsuario()
            );

            if (!imagenesGuardadas.isEmpty()) {
                existingReporte.setImagenes(imagenesGuardadas);
                logger.info("✅ Se guardaron {} nuevas imágenes", imagenesGuardadas.size());
            } else {
                logger.warn("⚠️ No se pudieron guardar las nuevas imágenes");
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

    // El resto de métodos siguen igual...
    /**
     * Versión mejorada para procesar imágenes con mejor manejo de diferentes formatos
     */
    private List<Imagen> procesarImagenesMejorado(List<?> imagenes, String reporteId, String usuarioId) {
        // El método permanece sin cambios
        List<Imagen> imagenesGuardadas = new ArrayList<>();
        logger.info("Procesando lista de {} imágenes para el reporte {}", imagenes.size(), reporteId);

        for (int i = 0; i < imagenes.size(); i++) {
            try {
                Object imagenInput = imagenes.get(i);
                logger.debug("Procesando imagen #{} de tipo {}", i+1,
                        imagenInput != null ? imagenInput.getClass().getName() : "null");

                if (imagenInput == null) {
                    logger.warn("⚠️ Imagen #{} es null, omitiendo", i+1);
                    continue;
                }

                // Crear una nueva imagen con información básica
                Imagen imagen = new Imagen();
                imagen.setId(UUID.randomUUID().toString());
                imagen.setReporteId(reporteId);
                imagen.setUsuarioId(usuarioId);
                imagen.setEstado(ESTADOREPORTE.Espera);
                imagen.setNombre("imagen_" + (i+1) + "_" + System.currentTimeMillis() + ".jpg");

                // Extraer contenido según el tipo de objeto
                byte[] contenido = null;

                if (imagenInput instanceof String) {
                    // Asumimos que es una cadena Base64
                    String base64 = (String) imagenInput;
                    contenido = convertirBase64ABytes(base64);
                    logger.debug("Procesando imagen como String (posible Base64)");
                }
                else if (imagenInput instanceof Imagen) {
                    // Es un objeto Imagen
                    Imagen imgObj = (Imagen) imagenInput;
                    if (imgObj.getNombre() != null) {
                        imagen.setNombre(imgObj.getNombre());
                    }
                    contenido = imgObj.getContent();
                    logger.debug("Procesando imagen como objeto Imagen");
                }
                else if (imagenInput instanceof Map) {
                    // Es un mapa con datos de imagen
                    Map<String, Object> imgMap = (Map<String, Object>) imagenInput;

                    // Extraer nombre si existe
                    if (imgMap.containsKey("nombre")) {
                        imagen.setNombre(String.valueOf(imgMap.get("nombre")));
                    }

                    // Extraer contenido - intentar varias claves comunes
                    if (imgMap.containsKey("base64")) {
                        contenido = convertirBase64ABytes(String.valueOf(imgMap.get("base64")));
                        logger.debug("Extrayendo contenido de clave 'base64'");
                    }
                    else if (imgMap.containsKey("url")) {
                        contenido = convertirBase64ABytes(String.valueOf(imgMap.get("url")));
                        logger.debug("Extrayendo contenido de clave 'url'");
                    }
                    else if (imgMap.containsKey("content") && imgMap.get("content") instanceof byte[]) {
                        contenido = (byte[]) imgMap.get("content");
                        logger.debug("Extrayendo contenido de clave 'content' (byte[])");
                    }
                    else if (imgMap.containsKey("data")) {
                        contenido = convertirBase64ABytes(String.valueOf(imgMap.get("data")));
                        logger.debug("Extrayendo contenido de clave 'data'");
                    }
                    else {
                        // Buscar cualquier clave que pueda contener Base64
                        for (String key : imgMap.keySet()) {
                            Object value = imgMap.get(key);
                            if (value instanceof String) {
                                String str = (String) value;
                                if (str.length() > 100 && (str.contains("base64") || str.contains("data:"))) {
                                    contenido = convertirBase64ABytes(str);
                                    logger.debug("Extrayendo contenido de clave '{}'", key);
                                    break;
                                }
                            }
                        }
                    }
                }
                else if (imagenInput instanceof ImagenDTO) {
                    // Es un ImagenDTO
                    ImagenDTO dto = (ImagenDTO) imagenInput;
                    if (dto.nombre() != null) {
                        imagen.setNombre(dto.nombre());
                    }
                    contenido = dto.content();
                    logger.debug("Procesando imagen como ImagenDTO");
                }
                else {
                    logger.warn("⚠️ Tipo de imagen no soportado: {}", imagenInput.getClass().getName());
                    continue;
                }

                // VERIFICACIÓN CRÍTICA: asegurarse de que hay contenido antes de guardar
                if (contenido == null || contenido.length == 0) {
                    logger.warn("⚠️ Imagen #{} NO tiene contenido, intentando procesar como Base64 genérico", i+1);

                    // Último intento: convertir cualquier objeto a string y ver si es Base64
                    if (imagenInput != null) {
                        String strValue = String.valueOf(imagenInput);
                        if (strValue.length() > 100) {  // Los Base64 suelen ser largos
                            contenido = convertirBase64ABytes(strValue);
                        }
                    }

                    if (contenido == null || contenido.length == 0) {
                        logger.warn("⚠️ Definitivamente Imagen #{} sin contenido válido, no se guardará", i+1);
                        continue;
                    }
                }

                // Ahora que tenemos contenido, lo asignamos
                imagen.setContent(contenido);

                // GUARDAR LA IMAGEN
                Imagen imagenGuardada = imagenRepository.save(imagen);
                imagenesGuardadas.add(imagenGuardada);
                logger.info("✅ Imagen #{} guardada con ID: {} y tamaño: {} bytes",
                        i+1, imagenGuardada.getId(), contenido.length);

            } catch (Exception e) {
                logger.error("❌ Error al procesar imagen #{}: {}", i+1, e.getMessage(), e);
            }
        }

        logger.info("Proceso finalizado: {} imágenes guardadas de {} recibidas",
                imagenesGuardadas.size(), imagenes.size());
        return imagenesGuardadas;
    }

    /**
     * Versión mejorada del método para convertir Base64 a bytes
     */
    private byte[] convertirBase64ABytes(String base64) {
        // El método permanece sin cambios
        try {
            if (base64 == null || base64.isEmpty()) {
                logger.debug("Base64 nulo o vacío");
                return null;
            }

            // Eliminar el prefijo "data:image/tipo;base64," si existe
            String contenidoBase64 = base64;
            if (base64.contains(",")) {
                contenidoBase64 = base64.substring(base64.indexOf(",") + 1);
            }

            // Eliminar espacios, saltos de línea y otros caracteres no válidos
            contenidoBase64 = contenidoBase64.replaceAll("\\s", "");

            try {
                byte[] bytes = Base64.getDecoder().decode(contenidoBase64);
                if (bytes.length > 0) {
                    logger.debug("Base64 decodificado correctamente, tamaño: {} bytes", bytes.length);
                    return bytes;
                } else {
                    logger.warn("Base64 decodificado pero resultado vacío");
                    return null;
                }
            } catch (IllegalArgumentException e) {
                logger.warn("Error al decodificar Base64: {}", e.getMessage());

                // Intento de recuperación para Base64 mal formados
                try {
                    String cleanBase64 = contenidoBase64.replaceAll("[^A-Za-z0-9+/=]", "");
                    return Base64.getDecoder().decode(cleanBase64);
                } catch (Exception ex) {
                    logger.error("Error incluso después de limpiar Base64", ex);
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error("Error general al procesar Base64: {}", e.getMessage());
            return null;
        }
    }
}