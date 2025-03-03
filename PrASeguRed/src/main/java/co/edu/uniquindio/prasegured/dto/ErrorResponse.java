package co.edu.uniquindio.prasegured.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String error,
        String message,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp,
        List<String> details
) {
    public ErrorResponse(HttpStatus status, String message, List<String> details) {
        this(status.value(), status.getReasonPhrase(), message, LocalDateTime.now(), details);
    }
}