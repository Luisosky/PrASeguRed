package co.edu.uniquindio.prasegured.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notificaciones")
public class Notificacion {
    
    @Id
    private String id;
    private String usuarioId;
    private String titulo;
    private String categoria;
    private String contenido;
    private boolean leido;
    private Date fechaCreacion;
    private String reporteId; // ID del reporte relacionado (si aplica)
    
    // Constructor vacío necesario para MongoDB
    public Notificacion() {
    }
    
    public Notificacion(String usuarioId, String titulo, String categoria, String contenido, String reporteId) {
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.categoria = categoria;
        this.contenido = contenido;
        this.leido = false;
        this.fechaCreacion = new Date();
        this.reporteId = reporteId;
    }
    
    // Getters y Setters
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUsuarioId() {
        return usuarioId;
    }
    
    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    public boolean isLeido() {
        return leido;
    }
    
    public void setLeido(boolean leido) {
        this.leido = leido;
    }
    
    public Date getFechaCreacion() {
        return fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    public String getReporteId() {
        return reporteId;
    }
    
    public void setReporteId(String reporteId) {
        this.reporteId = reporteId;
    }
}