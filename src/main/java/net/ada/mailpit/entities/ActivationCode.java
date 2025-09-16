package net.ada.mailpit.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String userEmail;

    private LocalDateTime expiration;

    public ActivationCode() {}

    public ActivationCode(String code, String userEmail, LocalDateTime expiration) {
        this.code = code;
        this.userEmail = userEmail;
        this.expiration = expiration;
    }

    // Getters et setters...

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(LocalDateTime expiration) {
        this.expiration = expiration;
    }
}
