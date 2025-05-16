package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.repository.ImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/imagenes")
@CrossOrigin("*")
public class ImagenController {

    @Autowired
    private ImagenRepository imagenRepository;

    /**
     * Endpoint para obtener una imagen por su ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerImagen(@PathVariable String id) {
        Optional<Imagen> imagenOpt = imagenRepository.findById(id);

        if (imagenOpt.isPresent()) {
            Imagen imagen = imagenOpt.get();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(determinarTipoContenido(imagen.getNombre()));

            return new ResponseEntity<>(imagen.getContent(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint para listar todas las imágenes de un reporte
     */
    @GetMapping("/reporte/{reporteId}")
    public ResponseEntity<?> listarImagenesPorReporte(@PathVariable String reporteId) {
        return ResponseEntity.ok(imagenRepository.findByReporteId(reporteId));
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