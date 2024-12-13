package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.FilterCriteriaDTO;
import rs.ac.uns.ftn.informatika.jpa.dto.ShowUserDTO;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails)
                .map(updatedUser -> new ResponseEntity<>(updatedUser, HttpStatus.OK))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/all/{id}")
    public ResponseEntity<List<ShowUserDTO>> getAllUsers(@PathVariable Long id) {
        List<ShowUserDTO> showUserDTOs = userService.getAllUsers(id);
        return ResponseEntity.ok(showUserDTOs);
    }
    @PostMapping("/filter/{id}")
    public ResponseEntity<List<ShowUserDTO>> filterUsers(@PathVariable Long id, @RequestBody FilterCriteriaDTO criteria) {

        List<ShowUserDTO> filteredUsers = userService.filterUsers
        (id,Optional.ofNullable(criteria.getName()),
                Optional.ofNullable(criteria.getSurname()),
                Optional.ofNullable(criteria.getEmail()),
                Optional.ofNullable(criteria.getMinPosts()),
                Optional.ofNullable(criteria.getMaxPosts()),
                Optional.ofNullable(criteria.getSortField()),
                Optional.ofNullable(criteria.getSortOrder()));
        return ResponseEntity.ok(filteredUsers);
    }

    @GetMapping("/show/{id}")
    public ResponseEntity<ShowUserDTO> getShowUserById(@PathVariable Long id) {
        return userService.getShowUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
