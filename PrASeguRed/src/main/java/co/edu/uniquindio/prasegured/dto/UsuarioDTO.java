package co.edu.uniquindio.prasegured.dto;

import java.util.List;

public class UsuarioDTO {
    private String id;
    private String nombreCom;
    private String telefono;
    private String ciudadResidencia;
    private String direccion;
    private String documento;
    private String fechaNacimiento;
    private String correo;
    private String rol; // Campo añadido para el rol
    private List<LocationDTO> locations; // Campo añadido para las ubicaciones

    // Constructor actualizado para incluir todos los campos
    public UsuarioDTO(String id, String nombreCom, String telefono, String ciudadResidencia,
                      String direccion, String documento, String fechaNacimiento, String correo,
                      String rol, List<LocationDTO> locations) {
        this.id = id;
        this.nombreCom = nombreCom;
        this.telefono = telefono;
        this.ciudadResidencia = ciudadResidencia;
        this.direccion = direccion;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.rol = rol;
        this.locations = locations;
    }

    // Getters y setters para rol
    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    // Getters y setters para locations
    public List<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDTO> locations) {
        this.locations = locations;
    }

    // Resto de getters y setters existentes
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCom() {
        return nombreCom;
    }

    public void setNombreCom(String nombreCom) {
        this.nombreCom = nombreCom;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCiudadResidencia() {
        return ciudadResidencia;
    }

    public void setCiudadResidencia(String ciudadResidencia) {
        this.ciudadResidencia = ciudadResidencia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}