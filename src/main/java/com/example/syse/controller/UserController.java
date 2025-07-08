package com.example.syse.controller;

import com.example.syse.dto.UserDto;
import com.example.syse.dto.RoleDto;
import com.example.syse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class UserController {
    @Autowired
    private UserService userService;

    // Xem danh sách user
    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    // Xem danh sách role
    @GetMapping("/roles")
    public List<RoleDto> getAllRoles() {
        return userService.getAllRoles();
    }

    // Gán role cho user
    @PostMapping("/users/{userId}/role")
    public Map<String, Object> updateUserRole(@PathVariable Integer userId, @RequestBody Map<String, String> req) {
        String roleName = req.get("role");
        boolean success = userService.updateUserRole(userId, roleName);
        
        if (success) {
            return Map.of("success", true, "message", "Role updated successfully");
        } else {
            return Map.of("success", false, "message", "User or role not found");
        }
    }
} 