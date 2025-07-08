package com.example.syse.controller;

import com.example.syse.model.Role;
import com.example.syse.model.User;
import com.example.syse.repository.RoleRepository;
import com.example.syse.repository.UserRepository;
import com.example.syse.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String password = req.get("password");
        String email = req.get("email");
        String fullName = req.get("fullName");

        if (userRepository.findByUsername(username).isPresent()) {
            return Map.of("success", false, "message", "Username already exists");
        }

        Role userRole = roleRepository.findByName("USER").orElseThrow();
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(userRole);
        user.setEnabled(true);
        userRepository.save(user);
        return Map.of("success", true, "message", "User registered successfully");
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> req) {
        try {
            String username = req.get("username");
            String password = req.get("password");

            // Authenticate user
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Load user with roles để lấy role information
            User user = userRepository.findByUsernameWithRole(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Extract role names - sửa để handle single role
            List<String> roles;
            if (user.getRole() != null) {
                roles = List.of(user.getRole().getName());
            } else {
                roles = List.of("USER"); // Default role
            }

            // Generate token with roles
            String token = jwtUtil.generateToken(username, roles);

            return Map.of(
                    "success", true,
                    "token", token,
                    "username", username,
                    "roles", roles
            );

        } catch (AuthenticationException e) {
            return Map.of("success", false, "message", "Invalid username or password");
        } catch (Exception e) {
            return Map.of("success", false, "message", "Login failed: " + e.getMessage());
        }
    }
}