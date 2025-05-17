package co.edu.uniquindio.prasegured.dto;

public class UsuarioDTO {
    private String id;
    private String nombreCom;
    private String telefono;
    private String ciudadResidencia;
    private String direccion;
    private String documento;
    private String fechaNacimiento;
    private String correo;

    // Constructor actualizado para incluir el ID
    public UsuarioDTO(String id, String nombreCom, String telefono, String ciudadResidencia,
                      String direccion, String documento, String fechaNacimiento, String correo) {
        this.id = id;
        this.nombreCom = nombreCom;
        this.telefono = telefono;
        this.ciudadResidencia = ciudadResidencia;
        this.direccion = direccion;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
    }

    // Getter y setter para el ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Resto de getters y setters existentes
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