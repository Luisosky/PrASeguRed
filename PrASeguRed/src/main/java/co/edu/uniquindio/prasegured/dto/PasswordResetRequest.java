package co.edu.uniquindio.prasegured.dto;

public class PasswordResetRequest {
    private String correo;
    private String codigo;
    private String nuevaContraseña;

    public PasswordResetRequest() {}

    public PasswordResetRequest(String correo, String codigo, String nuevaContraseña) {
        this.correo = correo;
        this.codigo = codigo;
        this.nuevaContraseña = nuevaContraseña;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNuevaContraseña() {
        return nuevaContraseña;
    }

    public void setNuevaContraseña(String nuevaContraseña) {
        this.nuevaContraseña = nuevaContraseña;
    }
}