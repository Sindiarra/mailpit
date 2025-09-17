package net.ada.mailpit.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.ada.mailpit.dtos.UserDTO;
import net.ada.mailpit.entities.User;
import net.ada.mailpit.repository.UserRepository;
import net.ada.mailpit.security.JwtUtil;
import net.ada.mailpit.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO) {
        String activationCode = userService.registerUser(userDTO);
        // TODO: envoyer email via Mailpit avec ce code
        return ResponseEntity.ok("Utilisateur créé. Code d'activation : " + activationCode);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        // Vérifier si l'utilisateur est activé
        User user = userRepository.findByEmail(userDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!user.isEnabled()) {
            return ResponseEntity.status(403).body("Compte non activé");
        }

        // Authentification
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);

        // Génération des tokens
        String accessToken = jwtUtil.generateAccessToken(userDTO.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(userDTO.getEmail());

        // Ajouter le refreshToken dans un cookie HttpOnly
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 7 jours
        response.addCookie(cookie);

        return ResponseEntity.ok(accessToken);
    }


    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@CookieValue("refreshToken") String refreshToken,
                                          HttpServletResponse response) {
        if (!jwtUtil.validateToken(refreshToken)) {
            return ResponseEntity.badRequest().body("Refresh token invalide");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        // Remplacer l'ancien cookie
        Cookie cookie = new Cookie("refreshToken", newRefreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);

        return ResponseEntity.ok(newAccessToken);
    }
}
