package co.edu.uniquindio.prasegured.auth;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenValidator {

    private final Key signingKey;

    public JwtTokenValidator() {
        Dotenv dotenv = Dotenv.configure().load();
        String secretKey = dotenv.get("JWT_SECRET");
        if (secretKey == null || secretKey.isBlank()) {
            secretKey = "myverysecureandlongenoughsecretkey123!"; // Fallback seguro
        }
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String token) {
        if (token == null) return null;
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (JwtException | IllegalArgumentException e) {
            return null; // Token inválido
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        if (token == null) return null;
        final Claims claims = extractAllClaims(token);
        if (claims == null) return null;
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        if (token == null) return false;
        try {
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenValid(String token, String username) {
        if (token == null || username == null) return false;
        String tokenUsername = extractUsername(token);
        return (tokenUsername != null && tokenUsername.equals(username) && isTokenValid(token));
    }

    private boolean isTokenExpired(String token) {
        if (token == null) return true;
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        if (token == null) return new Date(0); // Fecha expirada
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        if (token == null) return null;
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null; // Token inválido
        }
    }
}