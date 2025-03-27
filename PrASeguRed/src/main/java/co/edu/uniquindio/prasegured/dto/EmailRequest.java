package co.edu.uniquindio.prasegured.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailRequest {

    @JsonProperty("correo") // Mapea "correo" en JSON a "email" en Java
    private String email;

    public EmailRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
