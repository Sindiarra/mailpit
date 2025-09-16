package net.ada.mailpit.entities;

import jakarta.persistence.*;
import net.ada.mailpit.enums.Role;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String firstName;
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private boolean enabled = false; // activé après email
    private boolean locked = false;  // peut être utilisé plus tard (ex: sécurité)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ======= Constructeurs =======
    public User() {}

    public User(UUID id, String firstName, String lastName, String email, String phoneNumber,
                String password, Role role, boolean enabled, boolean locked,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.enabled = enabled;
        this.locked = locked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ======= Getters =======
    public UUID getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
    public boolean isEnabled() { return enabled; }
    public boolean isLocked() { return locked; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ======= Setters =======
    public void setId(UUID id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setLocked(boolean locked) { this.locked = locked; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ======= Builder alternatif (optionnel) =======
    public static UserBuilder builder() { return new UserBuilder(); }

    public static class UserBuilder {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String password;
        private Role role;
        private boolean enabled;
        private boolean locked;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public UserBuilder id(UUID id) { this.id = id; return this; }
        public UserBuilder firstName(String firstName) { this.firstName = firstName; return this; }
        public UserBuilder lastName(String lastName) { this.lastName = lastName; return this; }
        public UserBuilder email(String email) { this.email = email; return this; }
        public UserBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public UserBuilder password(String password) { this.password = password; return this; }
        public UserBuilder role(Role role) { this.role = role; return this; }
        public UserBuilder enabled(boolean enabled) { this.enabled = enabled; return this; }
        public UserBuilder locked(boolean locked) { this.locked = locked; return this; }
        public UserBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public UserBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public User build() {
            return new User(id, firstName, lastName, email, phoneNumber,
                    password, role, enabled, locked, createdAt, updatedAt);
        }
    }
}
