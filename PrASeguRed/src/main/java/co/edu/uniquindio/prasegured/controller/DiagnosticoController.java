package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/diagnostico")
@CrossOrigin("*")
public class DiagnosticoController {
    private static final Logger logger = LoggerFactory.getLogger(DiagnosticoController.class);

    @Autowired
    private ReporteRepository reporteRepository;

    @PostMapping("/reporte-request")
    public ResponseEntity<?> depurarReporteRequest(@RequestBody Object requestBody) {
        logger.info("Recibida solicitud para depuración: {}", requestBody);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Solicitud recibida y registrada en logs para análisis");
        respuesta.put("datosRecibidos", requestBody);

        if (requestBody instanceof Map) {
            Map<?, ?> mapBody = (Map<?, ?>) requestBody;
            if (mapBody.containsKey("imagenes")) {
                Object imagenes = mapBody.get("imagenes");
                respuesta.put("tieneImagenes", imagenes != null);
                if (imagenes instanceof List) {
                    List<?> listaImagenes = (List<?>) imagenes;
                    respuesta.put("cantidadImagenes", listaImagenes.size());

                    if (!listaImagenes.isEmpty()) {
                        Object primerElemento = listaImagenes.get(0);
                        respuesta.put("tipoImagenes", primerElemento != null ?
                                primerElemento.getClass().getName() : "null");
                    }
                }
            }
        }

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/reportes/{id}")
    public ResponseEntity<?> verificarReporte(@PathVariable String id) {
        Optional<Reporte> reporteOpt = reporteRepository.findById(id);

        if (reporteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reporte reporte = reporteOpt.get();
        Map<String, Object> detalles = new HashMap<>();
        detalles.put("id", reporte.getId());
        detalles.put("titulo", reporte.getTitulo());
        detalles.put("estado", reporte.getEstado());
        detalles.put("fechaCreacion", reporte.getFechaPublicacion());

        // Información sobre imágenes
        if (reporte.getImagenes() != null && !reporte.getImagenes().isEmpty()) {
            detalles.put("tieneImagenes", true);
            detalles.put("cantidadImagenes", reporte.getImagenes().size());

            List<Map<String, Object>> imagenesInfo = new ArrayList<>();
            for (int i = 0; i < reporte.getImagenes().size(); i++) {
                Imagen img = reporte.getImagenes().get(i);
                Map<String, Object> imgInfo = new HashMap<>();
                imgInfo.put("id", img.getId());
                imgInfo.put("nombre", img.getNombre());
                imgInfo.put("tieneContenido", img.getContent() != null && img.getContent().length > 0);
                imgInfo.put("tamanoContenido", img.getContent() != null ? img.getContent().length : 0);
                imgInfo.put("urlAcceso", "/reportes-imagenes/" + reporte.getId() + "/imagen/" + i);
                imagenesInfo.add(imgInfo);
            }
            detalles.put("imagenes", imagenesInfo);
        } else {
            detalles.put("tieneImagenes", false);
            detalles.put("cantidadImagenes", 0);
        }

        return ResponseEntity.ok(detalles);
    }
}