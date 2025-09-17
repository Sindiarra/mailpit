package net.ada.mailpit.scheduler;


import net.ada.mailpit.repository.ActivationCodeRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class ActivationCodeCleaner {

    private final ActivationCodeRepository codeRepository;

    public ActivationCodeCleaner(ActivationCodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    // Exécuté toutes les heures
    @Scheduled(cron = "0 0 * * * ?")
    @Transactional
    public void cleanExpiredCodes() {
        codeRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}
