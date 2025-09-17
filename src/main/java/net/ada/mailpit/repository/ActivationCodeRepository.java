package net.ada.mailpit.repository;

import net.ada.mailpit.entities.ActivationCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, UUID> {
    Optional<ActivationCode> findByCode(String code);
    void deleteByExpiryDateBefore(LocalDateTime date);

}
