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
import co.edu.uniquindio.prasegured.utils.ImageUtils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        logger.info("Iniciando guardado de reporte: {}", reporte.titulo());

        // Verificamos estructura de la solicitud con logs detallados
        logger.debug("Estructura de ReporteRequest: id={}, idUsuario={}, título={}, tiene imágenes={}",
                reporte.id(), reporte.idUsuario(), reporte.titulo(),
                reporte.imagenes() != null ? reporte.imagenes().size() : 0);

        if (reporte.imagenes() != null && !reporte.imagenes().isEmpty()) {
            // Intentamos mostrar información sobre el primer elemento para depuración
            Object primerElemento = reporte.imagenes().get(0);
            logger.debug("Primer elemento de imágenes es de tipo: {}",
                    primerElemento != null ? primerElemento.getClass().getName() : "null");
        }

        // Creamos una nueva instancia de Reporte
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
            logger.info("Procesando {} imágenes para el reporte {}",
                    reporte.imagenes().size(), savedReporte.getId());

            List<Imagen> imagenesGuardadas = procesarImagenesV2(
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
                // Limpiamos la lista de imágenes del reporte
                existingReporte.setImagenes(null);
                existingReporte = reporteRepository.save(existingReporte);
            }

            // Procesamos y guardamos las nuevas imágenes con la versión más robusta
            List<Imagen> imagenesGuardadas = procesarImagenesV2(
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

    /**
     * Nueva versión mejorada del procesador de imágenes, más robusta y con mejor
     * manejo de diferentes formatos y depuración
     */
    private List<Imagen> procesarImagenesV2(List<?> imagenes, String reporteId, String usuarioId) {
        List<Imagen> imagenesGuardadas = new ArrayList<>();
        logger.info("⬇️ INICIANDO PROCESAMIENTO DE {} IMÁGENES PARA REPORTE {} ⬇️",
                imagenes.size(), reporteId);

        if (imagenes.isEmpty()) {
            logger.warn("Lista de imágenes vacía, retornando");
            return imagenesGuardadas;
        }

        // Mostrar información diagnóstica sobre lo que estamos recibiendo
        logger.debug("Tipo de lista recibida: {}", imagenes.getClass().getName());
        logger.debug("Primer elemento tipo: {}",
                imagenes.get(0) != null ? imagenes.get(0).getClass().getName() : "null");

        // Intentar identificar el formato común en la lista
        String formatoDetectado = detectarFormatoImagenes(imagenes);
        logger.info("Formato detectado para las imágenes: {}", formatoDetectado);

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

                // Extraer contenido según el tipo de objeto y formato detectado
                byte[] contenido = null;

                // Basado en el formato detectado, aplicamos estrategias diferentes
                switch (formatoDetectado) {
                    case "STRING_BASE64":
                        if (imagenInput instanceof String) {
                            contenido = procesarStringBase64((String) imagenInput);
                        }
                        break;
                    case "MAP_CON_BASE64":
                        if (imagenInput instanceof Map) {
                            contenido = procesarMapConBase64((Map<String, Object>) imagenInput, imagen);
                        }
                        break;
                    case "OBJETO_IMAGEN":
                        if (imagenInput instanceof Imagen) {
                            contenido = procesarObjetoImagen((Imagen) imagenInput, imagen);
                        }
                        break;
                    case "IMAGEN_DTO":
                        if (imagenInput instanceof ImagenDTO) {
                            contenido = procesarImagenDTO((ImagenDTO) imagenInput, imagen);
                        }
                        break;
                    default:
                        // Si no se detectó un formato común, intentamos todos los métodos
                        contenido = procesarCualquierFormato(imagenInput, imagen);
                }

                // Si no se obtuvo contenido con el método específico, intenta el genérico
                if (contenido == null || contenido.length == 0) {
                    logger.warn("No se pudo extraer contenido con método específico, intentando método genérico");
                    contenido = procesarCualquierFormato(imagenInput, imagen);
                }

                // VERIFICACIÓN CRÍTICA: asegurarse de que hay contenido antes de guardar
                if (contenido == null || contenido.length == 0) {
                    logger.warn("⚠️ Imagen #{} NO tiene contenido después de todos los intentos, no se guardará", i+1);
                    continue;
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

        logger.info("⬆️ PROCESO FINALIZADO: {} IMÁGENES GUARDADAS DE {} RECIBIDAS ⬆️",
                imagenesGuardadas.size(), imagenes.size());
        return imagenesGuardadas;
    }

    /**
     * Detecta el formato predominante de las imágenes en la lista
     */
    private String detectarFormatoImagenes(List<?> imagenes) {
        if (imagenes.isEmpty()) return "DESCONOCIDO";

        // Tomar una muestra para análisis
        Object muestra = imagenes.get(0);

        if (muestra instanceof String) {
            String str = (String) muestra;
            if (str.length() > 100 && (str.contains("base64") || str.contains("data:"))) {
                return "STRING_BASE64";
            }
            return "STRING_OTRO";
        } else if (muestra instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) muestra;
            if (map.containsKey("base64") || map.containsKey("url") || map.containsKey("data")) {
                return "MAP_CON_BASE64";
            }
            return "MAP_OTRO";
        } else if (muestra instanceof Imagen) {
            return "OBJETO_IMAGEN";
        } else if (muestra instanceof ImagenDTO) {
            return "IMAGEN_DTO";
        }

        return "DESCONOCIDO";
    }

    /**
     * Procesa una cadena Base64
     */
    private byte[] procesarStringBase64(String base64) {
        if (base64 == null || base64.length() < 100) {
            logger.warn("Cadena recibida es demasiado corta para ser Base64 válido");
            return null;
        }

        logger.debug("Procesando String como Base64 (longitud: {} caracteres)", base64.length());
        return convertirBase64ABytes(base64);
    }

    /**
     * Procesa un Map que puede contener Base64
     */
    private byte[] procesarMapConBase64(Map<String, Object> map, Imagen imagen) {
        logger.debug("Procesando Map con claves: {}", map.keySet());

        // Si hay un nombre, lo asignamos
        if (map.containsKey("nombre") && map.get("nombre") != null) {
            imagen.setNombre(String.valueOf(map.get("nombre")));
        }

        // Buscamos el contenido en orden de prioridad
        if (map.containsKey("base64")) {
            return convertirBase64ABytes(String.valueOf(map.get("base64")));
        } else if (map.containsKey("url")) {
            return convertirBase64ABytes(String.valueOf(map.get("url")));
        } else if (map.containsKey("data")) {
            return convertirBase64ABytes(String.valueOf(map.get("data")));
        } else if (map.containsKey("content")) {
            if (map.get("content") instanceof byte[]) {
                return (byte[]) map.get("content");
            } else {
                return convertirBase64ABytes(String.valueOf(map.get("content")));
            }
        }

        // Buscar en cualquier campo que pueda contener Base64
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                String value = (String) entry.getValue();
                if (value.length() > 100 && (value.contains("base64") || value.contains("data:"))) {
                    logger.debug("Encontrado posible Base64 en campo '{}'", entry.getKey());
                    byte[] contenido = convertirBase64ABytes(value);
                    if (contenido != null && contenido.length > 0) {
                        return contenido;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Procesa un objeto Imagen
     */
    private byte[] procesarObjetoImagen(Imagen imagenObj, Imagen imagenDestino) {
        if (imagenObj.getNombre() != null) {
            imagenDestino.setNombre(imagenObj.getNombre());
        }
        return imagenObj.getContent();
    }

    /**
     * Procesa un objeto ImagenDTO
     */
    private byte[] procesarImagenDTO(ImagenDTO dto, Imagen imagen) {
        if (dto.nombre() != null) {
            imagen.setNombre(dto.nombre());
        }
        return dto.content();
    }

    /**
     * Intenta procesar cualquier formato de entrada para extraer contenido de imagen
     */
    private byte[] procesarCualquierFormato(Object imagenInput, Imagen imagen) {
        // Si es String, asumimos que es Base64
        if (imagenInput instanceof String) {
            return convertirBase64ABytes((String) imagenInput);
        }
        // Si es Map, buscamos campos conocidos
        else if (imagenInput instanceof Map) {
            return procesarMapConBase64((Map<String, Object>) imagenInput, imagen);
        }
        // Si es Imagen, tomamos su contenido directamente
        else if (imagenInput instanceof Imagen) {
            Imagen imgObj = (Imagen) imagenInput;
            if (imgObj.getNombre() != null) {
                imagen.setNombre(imgObj.getNombre());
            }
            return imgObj.getContent();
        }
        // Si es ImagenDTO, tomamos su contenido
        else if (imagenInput instanceof ImagenDTO) {
            ImagenDTO dto = (ImagenDTO) imagenInput;
            if (dto.nombre() != null) {
                imagen.setNombre(dto.nombre());
            }
            return dto.content();
        }
        // Último intento: convertir a String y ver si es Base64
        else {
            String strValue = String.valueOf(imagenInput);
            if (strValue.length() > 100) { // Los Base64 suelen ser largos
                return convertirBase64ABytes(strValue);
            }
        }

        return null;
    }

    /**
     * Versión mejorada del método para convertir Base64 a bytes
     * con mejor manejo de errores y depuración
     */
    private byte[] convertirBase64ABytes(String base64) {
        if (base64 == null || base64.isEmpty()) {
            logger.debug("Base64 nulo o vacío");
            return null;
        }

        try {
            // Mostrar información diagnóstica
            logger.debug("Procesando Base64 (longitud: {})", base64.length());

            // Preprocesamiento
            String contenidoBase64 = base64;

            // 1. Eliminar prefijo data:image si existe
            if (base64.contains(",")) {
                contenidoBase64 = base64.substring(base64.indexOf(",") + 1);
                logger.debug("Prefijo data:image eliminado");
            }

            // 2. Eliminar espacios y caracteres no válidos
            contenidoBase64 = contenidoBase64.replaceAll("\\s", "");

            // 3. Intentar decodificar
            try {
                byte[] bytes = Base64.getDecoder().decode(contenidoBase64);
                if (bytes.length > 0) {
                    logger.debug("Base64 decodificado correctamente: {} bytes", bytes.length);
                    return bytes;
                } else {
                    logger.warn("La decodificación Base64 produjo un array vacío");
                    return null;
                }
            } catch (IllegalArgumentException e) {
                logger.warn("Error al decodificar Base64 estándar: {}", e.getMessage());

                // Intento de recuperación #1: Limpiar caracteres no válidos
                try {
                    logger.debug("Intentando limpiar Base64...");
                    String cleanBase64 = contenidoBase64.replaceAll("[^A-Za-z0-9+/=]", "");
                    byte[] bytes = Base64.getDecoder().decode(cleanBase64);
                    if (bytes.length > 0) {
                        logger.debug("Base64 recuperado y decodificado: {} bytes", bytes.length);
                        return bytes;
                    }
                } catch (Exception ex) {
                    logger.debug("Error en intento de recuperación #1: {}", ex.getMessage());
                }

                // Intento de recuperación #2: Ajustar padding
                try {
                    logger.debug("Intentando ajustar padding...");
                    String paddedBase64 = contenidoBase64;
                    while (paddedBase64.length() % 4 != 0) {
                        paddedBase64 += "=";
                    }
                    byte[] bytes = Base64.getDecoder().decode(paddedBase64);
                    if (bytes.length > 0) {
                        logger.debug("Base64 con padding ajustado decodificado: {} bytes", bytes.length);
                        return bytes;
                    }
                } catch (Exception ex) {
                    logger.debug("Error en intento de recuperación #2: {}", ex.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Error general al procesar Base64: {}", e.getMessage());
        }

        logger.warn("Todos los intentos de decodificación Base64 fallaron");
        return null;
    }

    /**
     * Este método se mantiene para compatibilidad con código existente
     * pero ahora llama a la versión V2 mejorada
     */
    private List<Imagen> procesarImagenesMejorado(List<?> imagenes, String reporteId, String usuarioId) {
        return procesarImagenesV2(imagenes, reporteId, usuarioId);
    }

    @Override
    public List<ReporteDTO> findByUserId(String userId) {
        List<Reporte> reportes = reporteRepository.findByIdUsuario(userId);

        return reportes.stream()
                .map(reporteMapper::toReporteDTO)  // Usar reporteMapper en lugar de this::convertToDTO
                .collect(Collectors.toList());
    }
}