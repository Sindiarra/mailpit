package net.ada.mailpit.web;

import net.ada.mailpit.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ActivationController {

    private final UserService userService;

    public ActivationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activate(@RequestParam String code) {
        if (userService.activateAccount(code)) {
            return ResponseEntity.ok("Compte activé avec succès !");
        } else {
            return ResponseEntity.badRequest().body("Code d'activation invalide ou expiré.");
        }
    }

}
