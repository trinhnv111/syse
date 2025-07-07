package com.example.syse.controller;

import com.example.syse.model.User;
import com.example.syse.model.Role;
import com.example.syse.repository.UserRepository;
import com.example.syse.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    // Xem danh sách user
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Xem danh sách role
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Gán role cho user
    @PostMapping("/users/{userId}/role")
    public Map<String, Object> updateUserRole(@PathVariable Integer userId, @RequestBody Map<String, String> req) {
        String roleName = req.get("role");
        User user = userRepository.findById(userId).orElse(null);
        Role role = roleRepository.findByName(roleName).orElse(null);
        if (user == null || role == null) {
            return Map.of("success", false, "message", "User or role not found");
        }
        user.setRole(role);
        userRepository.save(user);
        return Map.of("success", true, "message", "Role updated");
    }
} 