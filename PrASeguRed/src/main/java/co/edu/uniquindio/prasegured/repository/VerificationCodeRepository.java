package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.AuthCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VerificationCodeRepository extends MongoRepository<AuthCode, String> {
    Optional<AuthCode> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email);
}