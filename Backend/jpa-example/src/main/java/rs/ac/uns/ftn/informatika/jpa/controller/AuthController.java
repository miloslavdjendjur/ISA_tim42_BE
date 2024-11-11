package rs.ac.uns.ftn.informatika.jpa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.informatika.jpa.dto.LoginRequest;
import rs.ac.uns.ftn.informatika.jpa.model.User;
import rs.ac.uns.ftn.informatika.jpa.service.CustomUserDetails;
import rs.ac.uns.ftn.informatika.jpa.service.UserService;
import rs.ac.uns.ftn.informatika.jpa.util.JwtUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        if (userService.emailExists(user.getEmail())) {
            response.put("error", "A user with this email already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (userService.usernameExists(user.getUsername())) {
            response.put("error", "A user with this username already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        userService.registerUser(user);
        response.put("message", "User registered successfully. Please check your email for activation link.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/activate")
    public ResponseEntity<Map<String, String>> activateAccount(@RequestParam("token") String token) {
        System.out.println("Activation endpoint hit with token: " + token); // Log each request
        Map<String, String> response = new HashMap<>();
        int activationResult = userService.activateUser(token);

        if (activationResult == 1) {
            response.put("message", "Account activated successfully. You can now log in.");
            return ResponseEntity.ok(response); // HTTP 200 for success
        } else if (activationResult == -1) {
            response.put("error", "This account has already been activated.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response); // HTTP 409 for already active
        } else {
            response.put("error", "Invalid or expired activation token.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // HTTP 400 for invalid token
        }
    }





    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();  // Access custom User fields

            String token = jwtUtil.generateToken(authentication);

            response.put("token", token);
            response.put("id", user.getId());
            response.put("email", user.getEmail());
            response.put("username", user.getUsername());
            response.put("fullName", user.getFullName());
            response.put("role", user.getRole().name());
            response.put("activated", user.getActive());
            response.put("message", "Login successful!");

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            response.put("message", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}






