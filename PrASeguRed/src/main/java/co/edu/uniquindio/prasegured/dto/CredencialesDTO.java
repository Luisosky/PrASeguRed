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
}