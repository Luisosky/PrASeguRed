package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.model.VerificationCode;
import co.edu.uniquindio.prasegured.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationService {
    private final VerificationCodeRepository verificationCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public VerificationService(VerificationCodeRepository verificationCodeRepository) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public void storeVerificationCode(String email, String code) {
        // Borrar códigos anteriores
        verificationCodeRepository.deleteByEmail(email);

        // Opcion 1: Almacenar codigo en texto plano
        //VerificationCode verificationCode = new VerificationCode(
        //        email,
        //        code,
        //        LocalDateTime.now().plusHours(1)  // Codigo expira en 1 hora
        //);

        // Opcion 2: Almacenar codigo hasheado
         VerificationCode verificationCode = new VerificationCode(
           email,
            passwordEncoder.encode(code),
           LocalDateTime.now().plusHours(1)
         );

        verificationCodeRepository.save(verificationCode);
    }

    public boolean verifyCode(String email, String code) {
        Optional<VerificationCode> storedCode = verificationCodeRepository.findByEmailAndUsedFalse(email);

        if (storedCode.isEmpty() || storedCode.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        // Opcion 1: Por el texto plano
        //boolean isValid = storedCode.get().getCode().equals(code);

        // Opcion 2: Por el codigo hasheado
        boolean isValid = passwordEncoder.matches(code, storedCode.get().getCode());

        if (isValid) {
            // Marcar el codigo como usado
            VerificationCode verificationCode = storedCode.get();
            verificationCode.setUsed(true);
            verificationCodeRepository.save(verificationCode);
        }

        return isValid;
    }

    public void clearVerificationCode(String email) {
        verificationCodeRepository.deleteByEmail(email);
    }
}
