package co.edu.uniquindio.prasegured.dto;

public class UsuarioDTO {
    private String nombreCom;
    private String telefono;
    private String ciudadResidencia;
    private String direccion;
    private String documento;
    private String fechaNacimiento;

    // Constructor
    public UsuarioDTO(String nombreCom, String telefono, String ciudadResidencia, String direccion, String documento, String fechaNacimiento) {
        this.nombreCom = nombreCom;
        this.telefono = telefono;
        this.ciudadResidencia = ciudadResidencia;
        this.direccion = direccion;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
    }

    // Getters and Setters
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
}