package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.model.AuthCode;
import co.edu.uniquindio.prasegured.repository.VerificationCodeRepository;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;

    public AuthService(VerificationCodeRepository verificationCodeRepository, EmailService emailService) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.emailService = emailService;
    }

    public void sendVerificationCode(String email) throws MessagingException {
        // Eliminar códigos anteriores del usuario
        verificationCodeRepository.deleteByEmail(email);

        // Generar un código aleatorio de 6 dígitos
        String code = String.format("%06d", new Random().nextInt(999999));

        // Guardar en la BD con una expiración de 15 minutos
        AuthCode verificationCode = new AuthCode();
        verificationCode.setEmail(email);
        verificationCode.setCode(code);
        verificationCode.setExpirationTime(LocalDateTime.now().plusMinutes(15));

        verificationCodeRepository.save(verificationCode);

        // Enviar correo electrónico
        emailService.sendVerificationCode(email, code);
    }

    public boolean verifyCode(String email, String code) {
        return verificationCodeRepository.findByEmailAndCode(email, code)
                .filter(vc -> vc.getExpirationTime().isAfter(LocalDateTime.now()))
                .isPresent();
    }
}