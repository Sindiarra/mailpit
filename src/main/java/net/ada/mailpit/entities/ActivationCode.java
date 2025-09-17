package net.ada.mailpit.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "activation_codes")
public class ActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ----------------------------
    // Constructeurs
    // ----------------------------
    public ActivationCode() {}

    public ActivationCode(UUID id, String code, LocalDateTime expiryDate, User user) {
        this.id = id;
        this.code = code;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    // ----------------------------
    // Getters & Setters
    // ----------------------------
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
