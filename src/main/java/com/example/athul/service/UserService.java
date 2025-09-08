package com.example.athul.service;

import com.example.athul.model.User;
import com.example.athul.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // ✅ Seed an admin user if not present
    @PostConstruct
    public void seedAdmin() {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setFullName("Admin");
            admin.setEmail("admin@example.com");
            admin.setPhone("1234567890");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole("ADMIN");
            admin.setUserType("ADMIN");
            userRepository.save(admin);
            System.out.println("✅ Admin user seeded successfully.");
        }
    }

    // ✅ Register user with unique email check
    public void registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        if (user.getActiveStatus() == null) {
            user.setActiveStatus("active");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getUserType().equalsIgnoreCase("Client") ? "CLIENT" : "FREELANCER");

        userRepository.save(user);
    }

    // ✅ Authenticate user
    public void authenticateUser(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    // ✅ Retrieve user details by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // ✅ Forgot password functionality
    public void forgotPassword(String email) {
        User user = findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found"));
        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        userRepository.save(user);
        emailService.sendEmail(email, "Password Reset Request", "Click below to reset password:\nhttp://localhost:3000/reset-password?token=" + resetToken);
    }

    // ✅ Reset password functionality
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        userRepository.save(user);
    }

    // ✅ Get all users (Admin Only)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Delete user by ID
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public long getTotalUserCount() {
        return userRepository.count();
    }

    public long getTotalFreelancerCount() {
        return userRepository.countByUserType("freelancer");
    }

    public long getTotalClientCount() {
        return userRepository.countByUserType("client");
    }

    public Optional<User> getFreelancerByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.filter(u -> "FREELANCER".equalsIgnoreCase(u.getRole()));
    }

    // ✅ Get users by role (Newly added)
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role.toUpperCase()); // case-insensitive match
    }
}
