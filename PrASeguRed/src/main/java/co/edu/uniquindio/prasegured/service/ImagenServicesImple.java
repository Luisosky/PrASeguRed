package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.model.Imagen;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class ImagenServicesImple implements ImagenService {
    @Override
    public ImagenDTO saveImagen(MultipartFile file) throws IOException {
        return null;
    }

    @Override
    public void deleteImagen(String id) throws IOException {

    }

    @Override
    public ImagenDTO updateImagen(String id, MultipartFile file) throws IOException {
        return null;
    }

    @Override
    public Void estadoDenegado(String id) throws IOException {
        return null;
    }

    @Override
    public List<Imagen> getImagenReporte(String reporteId) throws IOException {
        return List.of();
    }

    @Override
    public List<Imagen> getImagenesUsuario(String usuarioId) throws IOException {
        return List.of();
    }
}
