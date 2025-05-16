package co.edu.uniquindio.prasegured.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Base64;

public class ImageUtils {
    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * Convierte una cadena Base64 a un arreglo de bytes
     */
    public static byte[] convertirBase64ABytes(String base64) {
        try {
            if (base64 == null || base64.isEmpty()) {
                logger.warn("La cadena Base64 está vacía o es nula");
                return new byte[0];
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

    /**
     * Extrae el tipo MIME desde una cadena Base64 con prefijo
     */
    public static String extraerTipoMIME(String base64) {
        if (base64 == null || !base64.contains("data:") || !base64.contains(";base64")) {
            return "image/jpeg"; // Por defecto
        }

        try {
            String tipoMIME = base64.substring(base64.indexOf("data:") + 5, base64.indexOf(";base64"));
            return tipoMIME.isEmpty() ? "image/jpeg" : tipoMIME;
        } catch (Exception e) {
            logger.error("Error al extraer tipo MIME de Base64", e);
            return "image/jpeg";
        }
    }
}