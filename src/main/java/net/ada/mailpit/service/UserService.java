package net.ada.mailpit.service;


import net.ada.mailpit.dtos.UserDTO;
import net.ada.mailpit.entities.ActivationCode;
import net.ada.mailpit.entities.User;
import net.ada.mailpit.enums.Role;
import net.ada.mailpit.repository.ActivationCodeRepository;
import net.ada.mailpit.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ActivationCodeRepository activationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository,
                       ActivationCodeRepository activationCodeRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.activationCodeRepository = activationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Inscription + génération du code d’activation + envoi du mail
     */
    public String registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(false);
        user.setLocked(false);

        userRepository.save(user);

        String activationCodeStr = UUID.randomUUID().toString();
        ActivationCode code = new ActivationCode();
        code.setCode(activationCodeStr);
        code.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        code.setUser(user);

        activationCodeRepository.save(code);

        // Envoi du mail avec le code
        emailService.sendActivationEmail(user.getEmail(), activationCodeStr);

        return activationCodeStr;
    }

    /**
     *  Activation du compte
     */
    @Transactional
    public boolean activateAccount(String codeStr) {
        ActivationCode activationCode = activationCodeRepository.findByCode(codeStr)
                .orElseThrow(() -> new RuntimeException("Code invalide"));

        if (activationCode.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Code expiré");
        }

        User user = activationCode.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        activationCodeRepository.delete(activationCode);

        return true;
    }

    // -----------------------
    // CRUD
    // -----------------------

    // Créer un utilisateur
    public User createUser(UserDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setLocked(false);
        return userRepository.save(user);
    }

    // Lire tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Lire un utilisateur par ID
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }

    // Mettre à jour un utilisateur
    @Transactional
    public User updateUser(UUID id, UserDTO dto) {
        User user = getUserById(id);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return userRepository.save(user);
    }

    // Supprimer un utilisateur
    public void deleteUser(UUID id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
