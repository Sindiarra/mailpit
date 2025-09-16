package net.ada.mailpit.service;

import net.ada.mailpit.dtos.UserDTO;
import net.ada.mailpit.entities.User;
import net.ada.mailpit.entities.ActivationCode;
import net.ada.mailpit.repository.UserRepository;
import net.ada.mailpit.repository.ActivationCodeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ActivationCodeRepository codeRepository;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       ActivationCodeRepository codeRepository,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.codeRepository = codeRepository;
        this.emailService = emailService;
    }

    public String registerUser(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // Hasher si nécessaire
        user.setRole(userDTO.getRole());
        user.setEnabled(false); // compte désactivé par défaut
        userRepository.save(user);

        String code = UUID.randomUUID().toString();
        ActivationCode activationCode = new ActivationCode(code, user.getEmail(), LocalDateTime.now().plusHours(24));
        codeRepository.save(activationCode);

        emailService.sendActivationEmail(user.getEmail(), code);

        return code;
    }

    public boolean activateUser(String code) {
        return codeRepository.findByCode(code).map(c -> {
            userRepository.findByEmail(c.getUserEmail()).ifPresent(u -> u.setEnabled(true));
            userRepository.flush();
            codeRepository.delete(c);
            return true;
        }).orElse(false);
    }
}
