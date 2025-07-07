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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.get("username"), req.get("password"))
            );
            String token = jwtUtil.generateToken(req.get("username"));
            return Map.of("success", true, "token", token);
        } catch (AuthenticationException e) {
            return Map.of("success", false, "message", "Invalid username or password");
        }
    }
} 