package com.example.athul.controller;

import com.example.athul.dto.*;
import com.example.athul.model.User;
import com.example.athul.security.JwtUtil;
import com.example.athul.service.EmailService;
import com.example.athul.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtUtil jwtUtil, EmailService emailService,
                          BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // ✅ User Registration
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody User user) {
        try {
            if (user.getUserType() == null || user.getUserType().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseMessage("❌ User type is required"));
            }

            if (!"CLIENT".equalsIgnoreCase(user.getUserType()) && !"FREELANCER".equalsIgnoreCase(user.getUserType())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseMessage("❌ Invalid user type"));
            }

            userService.registerUser(user);
            return ResponseEntity.ok(new ResponseMessage("✅ User registered successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("❌ " + e.getMessage()));
        }
    }

    // ✅ Get Logged-in User Profile
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }
    }

    // ✅ User Login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        System.out.println("🔍 Login Attempt: " + loginRequest.getEmail());

        Optional<User> userOptional = userService.findByEmail(loginRequest.getEmail());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseMessage("❌ User not found"));
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseMessage("❌ Invalid credentials"));
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", user.getRole(),
                "userType", user.getUserType(),
                "id", user.getId(),
                "fullName", user.getFullName(),
                "email", user.getEmail()
        ));
    }

    // ✅ Forgot Password
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseMessage> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            userService.forgotPassword(request.getEmail());
            return ResponseEntity.ok(new ResponseMessage("📧 Password reset link sent."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("❌ " + e.getMessage()));
        }
    }

    // ✅ Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            userService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(new ResponseMessage("🔑 Password reset successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("❌ " + e.getMessage()));
        }
    }

    // ✅ Get All Users (Admin Only)
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ✅ Delete User (Admin Only)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ResponseMessage("🗑️ User deleted successfully."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("❌ " + e.getMessage()));
        }
    }

    // ✅ Get user counts
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUserCounts() {
        long totalUsers = userService.getTotalUserCount();
        long totalFreelancers = userService.getTotalFreelancerCount();
        long totalClients = userService.getTotalClientCount();

        Map<String, Long> counts = Map.of(
            "totalUsers", totalUsers,
            "totalFreelancers", totalFreelancers,
            "totalClients", totalClients
        );

        return ResponseEntity.ok(counts);
    }

    // ✅ Get freelancer profile
    @GetMapping("/freelancer/profile")
    public ResponseEntity<?> getFreelancerProfile(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if ("FREELANCER".equalsIgnoreCase(user.getRole())) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Access Denied: Not a Freelancer"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Freelancer not found"));
        }
    }

    // ✅ Get all freelancers
    @GetMapping("/freelancers")
    public ResponseEntity<List<User>> getAllFreelancers() {
        List<User> freelancers = userService.getUsersByRole("freelancer");
        return ResponseEntity.ok(freelancers);
    }

    // ✅ Get all clients
    @GetMapping("/clients")
    public ResponseEntity<List<User>> getAllClients() {
        List<User> clients = userService.getUsersByRole("client");
        return ResponseEntity.ok(clients);
    }
}
