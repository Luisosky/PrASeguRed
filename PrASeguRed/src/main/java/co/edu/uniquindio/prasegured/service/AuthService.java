package co.edu.uniquindio.prasegured.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public String authenticateUser(String email, String password) {
        // TODO: Implement user authentication
        // Generate JWT token
        return "generated-jwt-token";
    }
}