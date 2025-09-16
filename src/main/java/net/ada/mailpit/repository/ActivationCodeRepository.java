package net.ada.mailpit.repository;

import net.ada.mailpit.entities.ActivationCode;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActivationCodeRepository extends JpaRepository<ActivationCode, UUID> {
    Optional<ActivationCode> findByCode(String code);
    void deleteByExpirationBefore(java.time.LocalDateTime now);
}
