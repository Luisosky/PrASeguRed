package co.edu.uniquindio.prasegured.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "reports")
public class Report {

    @Id
    private String id;
    private String titulo;
    private Integer limiteEdad;
    private String descripcion;
    private String ubicacion;
    private boolean importante;
    private boolean resuelto;
    private List<String> imagenes;
    private String categoria;
    private String userId;

    public Report() {}

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Integer getLimiteEdad() { return limiteEdad; }
    public void setLimiteEdad(Integer limiteEdad) { this.limiteEdad = limiteEdad; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public boolean isImportante() { return importante; }
    public void setImportante(boolean importante) { this.importante = importante; }

    public boolean isResuelto() { return resuelto; }
    public void setResuelto(boolean resuelto) { this.resuelto = resuelto; }

    public List<String> getImagenes() { return imagenes; }
    public void setImagenes(List<String> imagenes) { this.imagenes = imagenes; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}