package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/reportes-imagenes")
@CrossOrigin("*")
public class ImagenController {

    @Autowired
    private ReporteRepository reporteRepository;

    /**
     * Endpoint para obtener una imagen embebida directamente del documento de reporte
     */
    @GetMapping("/{reporteId}/imagen/{indice}")
    public ResponseEntity<?> obtenerImagenEmbebida(@PathVariable String reporteId, @PathVariable int indice) {
        // Buscar el reporte directamente
        Optional<Reporte> reporteOpt = reporteRepository.findById(reporteId);

        if (reporteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reporte reporte = reporteOpt.get();

        // Verificar si el reporte tiene imágenes y el índice es válido
        if (reporte.getImagenes() == null || reporte.getImagenes().isEmpty() || indice >= reporte.getImagenes().size()) {
            return ResponseEntity.notFound().build();
        }

        // Obtener la imagen por índice
        Imagen imagen = reporte.getImagenes().get(indice);

        // Verificar si la imagen tiene contenido
        if (imagen.getContent() == null || imagen.getContent().length == 0) {
            return ResponseEntity.noContent().build();
        }

        // Configurar los headers para la respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(determinarTipoContenido(imagen.getNombre()));

        // Devolver el contenido de la imagen
        return new ResponseEntity<>(imagen.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Determina el tipo de contenido basado en el nombre del archivo
     */
    private MediaType determinarTipoContenido(String nombreArchivo) {
        if (nombreArchivo == null) {
            return MediaType.IMAGE_JPEG;
        }

        nombreArchivo = nombreArchivo.toLowerCase();

        if (nombreArchivo.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (nombreArchivo.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else if (nombreArchivo.endsWith(".webp")) {
            return MediaType.parseMediaType("image/webp");
        } else if (nombreArchivo.endsWith(".svg")) {
            return MediaType.parseMediaType("image/svg+xml");
        } else {
            return MediaType.IMAGE_JPEG;
        }
    }
}