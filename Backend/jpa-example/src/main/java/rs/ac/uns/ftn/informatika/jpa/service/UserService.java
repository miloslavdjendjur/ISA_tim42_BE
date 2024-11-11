package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setFullName(userDetails.getFullName());
            user.setAddress(userDetails.getAddress());
            user.setRole(userDetails.getRole());
            return userRepository.save(user);
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User registerUser(User user) {
        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Generate verification token and set user as inactive
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        user.setActive(false);

        user.setRole(User.Role.REGISTERED);
        // Save the user
        User savedUser = userRepository.save(user);

        // Send the activation email
        emailService.sendActivationEmail(user.getEmail(), token);

        return savedUser;
    }

    public int activateUser(String token) {
        Optional<User> userOptional = userRepository.findByVerificationToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getActive()) {
                user.setActive(true);
                user.setVerificationToken(null); // Clear token to prevent reuse
                userRepository.save(user);
                return 1; // Successfully activated
            } else {
                return -1; // User already active
            }
        } else {
            return 0; // Invalid token
        }
    }



    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }



}
