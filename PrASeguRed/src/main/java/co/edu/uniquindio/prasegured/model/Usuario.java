package co.edu.uniquindio.prasegured.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    private String tpDocumento;
    private String documento;
    private String nombreCom;
    private String fechaNacimiento;
    private String ciudadResidencia;
    private String direccion;
    private String telefono;
    private ROL rol;
    private ESTADOSUSUARIO estado;
    private String correo;
    private String preferencias;
    private String contraseña;
    private List<Location> locations;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTpDocumento() {
        return tpDocumento;
    }

    public void setTpDocumento(String tpDocumento) {
        this.tpDocumento = tpDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombreCom() {
        return nombreCom;
    }

    public void setNombreCom(String nombreCom) {
        this.nombreCom = nombreCom;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol != null ? rol.toString() : null;
    }

    // Con @JsonIgnore, cualquier valor enviado en el JSON para el rol será ignorado
    // El servicio establecerá el rol mediante este método
    @JsonIgnore
    public void setRol(String rol) {
        if (rol != null) {
            try {
                this.rol = ROL.valueOf(rol);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de rol inválido: " + rol);
            }
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado != null ? estado.toString() : null;
    }

    public void setEstado(String estado) {
        if (estado != null) {
            try {
                this.estado = ESTADOSUSUARIO.valueOf(estado);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Valor de estado inválido: " + estado);
            }
        }
    }

    public String getPreferencias() {
        return preferencias;
    }

    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}