package co.edu.uniquindio.prasegured.dto;

public record CredencialesDTO(String correo, String contraseña) {
    @Override
    public String correo() {
        return correo;
    }

    @Override
    public String contraseña() {
        return contraseña;
    }

    @Override
    public String toString() {
        return "CredencialesDTO{" +
                "correo='" + correo + '\'' +
                ", contraseña='" + contraseña + '\'' +
                '}';
    }
}