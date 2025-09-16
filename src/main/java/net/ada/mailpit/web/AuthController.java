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
    public ResponseEntity<String> login(@RequestParam String email,
                                        @RequestParam String password,
                                        HttpServletResponse response) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(auth);

        String accessToken = jwtUtil.generateAccessToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);

        // Ajouter refreshToken dans un cookie HttpOnly
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
