package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.VerificationCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends MongoRepository<VerificationCode, String> {
    Optional<VerificationCode> findByEmail(String email);
    Optional<VerificationCode> findByEmailAndUsedFalse(String email);
    void deleteByEmail(String email);
    Optional<VerificationCode> findByCode(String code);
}