package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.dto.ShowUserDTO;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.PostRepository;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository,PostRepository postRepository, PasswordEncoder passwordEncoder, EmailService emailService) {

        this.userRepository = userRepository;
        this.postRepository = postRepository;
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
    public List<ShowUserDTO> getAllUsers(Long adminId){
        List<User> users = userRepository.findAll();
        List<ShowUserDTO> showUserDTOs = new ArrayList<>();
        for (User user : users) {
            if(!user.getId().equals(adminId)) {
                long postCount = postRepository.countByUserId(user.getId());
                ShowUserDTO showUserDTO = new ShowUserDTO(
                        user.getId(),user.getFullName(),user.getEmail(),postCount,user.getFollowersCount()
                );
                showUserDTOs.add(showUserDTO);
            }
        }
        return showUserDTOs;
    }
    public List<ShowUserDTO> filterUsers(Long adminId, Optional<String> name, Optional<String> surname, Optional<String> email,
                                         Optional<Integer> minPosts, Optional<Integer> maxPosts,
                                         Optional<String> sortField, Optional<String> sortOrder) {
        List<ShowUserDTO> showUserDTOs = getAllUsers(adminId);

        return showUserDTOs.stream()
                .filter(user -> name.map(n -> user.getFullName().toLowerCase().contains(n.toLowerCase())).orElse(true))
                .filter(user -> surname.map(s -> user.getFullName().toLowerCase().contains(s.toLowerCase())).orElse(true))
                .filter(user -> email.map(e -> user.getEmail().toLowerCase().contains(e.toLowerCase())).orElse(true))
                .filter(user -> minPosts.map(min -> user.getPostNumber() >= min).orElse(true))
                .filter(user -> maxPosts.map(max -> user.getPostNumber() <= max).orElse(true))

                .sorted((u1, u2) -> {
                    if (!sortField.isPresent()) return 0;
                    int direction = sortOrder.orElse("asc").equalsIgnoreCase("asc") ? 1 : -1;

                    switch (sortField.get().toLowerCase()) {
                        case "followers":
                            return direction * Long.compare(u1.getFollowsPeople(), u2.getFollowsPeople());
                        case "email":
                            return direction * u1.getEmail().compareToIgnoreCase(u2.getEmail());
                        default:
                            return 0;
                    }
                })
                .collect(Collectors.toList());
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public Optional<ShowUserDTO> getShowUserById(Long id) {
        return userRepository.findById(id)
                .map(user -> new ShowUserDTO(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        postRepository.countByUserId(user.getId()), // Count the number of posts
                        user.getFollowersCount() // Count the number of followers
                ));
    }


}
