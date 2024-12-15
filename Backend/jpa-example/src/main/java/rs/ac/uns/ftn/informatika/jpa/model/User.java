package rs.ac.uns.ftn.informatika.jpa.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 20)
    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    private String address;

    @Column(nullable = false)
    private boolean isActive = false;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(nullable = false)
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private int numberOfPeopleFollowing = 0;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private boolean activated = false;

    @Version
    private Integer version;

    public enum Role {
        UNAUTHENTICATED, REGISTERED, ADMIN
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    public int getNumberOfPeopleFollowing() {
        return numberOfPeopleFollowing;
    }

    public void setNumberOfPeopleFollowing(int numberOfPeopleFollowing) {
        this.numberOfPeopleFollowing = numberOfPeopleFollowing;
    }

    public void setActive(boolean active) { this.isActive = active; }

    public boolean getActive() { return this.isActive; }

    public void setVerificationToken(String token) { this.verificationToken = token; }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
