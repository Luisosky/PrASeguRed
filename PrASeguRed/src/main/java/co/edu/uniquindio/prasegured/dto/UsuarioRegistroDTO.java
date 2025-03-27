package co.edu.uniquindio.prasegured.dto;

import java.time.LocalDate;

public record UsuarioRegistroDTO(
        String tpDocumento,
        String documento,
        String nombreCom,
        LocalDate fechaNacimiento,
        String ciudadResidencia,
        String direccion,
        String telefono,
        String cargo,
        String estado,
        String correo,
        String preferencias,
        String contraseña
) {}