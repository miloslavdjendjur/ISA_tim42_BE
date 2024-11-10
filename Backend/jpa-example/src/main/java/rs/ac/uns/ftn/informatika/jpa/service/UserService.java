package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseGet(() -> {
            User defaultUser = new User();
            defaultUser.setId(-1L);
            defaultUser.setUsername("unknown_user");
            return defaultUser;
        });
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
}
