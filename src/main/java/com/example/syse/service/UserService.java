package com.example.syse.service;

import com.example.syse.dto.UserDto;
import com.example.syse.dto.RoleDto;
import com.example.syse.model.User;
import com.example.syse.model.Role;
import com.example.syse.repository.UserRepository;
import com.example.syse.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    // Convert User entity to UserDto
    public UserDto convertToDto(User user) {
        if (user == null) return null;
        
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setStatus(user.getEnabled());
        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getId().longValue());
        }
        
        return dto;
    }

    // Convert Role entity to RoleDto
    public RoleDto convertToDto(Role role) {
        if (role == null) return null;
        
        return new RoleDto(
            role.getId(),
            role.getName(),
            role.getDescription()
        );
    }

    // Get all users as DTOs
    public List<UserDto> getAllUsers() {
        return userRepository.findAllWithRoles().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Get all roles as DTOs
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Update user role
    public boolean updateUserRole(Integer userId, String roleName) {
        User user = userRepository.findById(userId).orElse(null);
        Role role = roleRepository.findByName(roleName).orElse(null);
        
        if (user == null || role == null) {
            return false;
        }
        
        user.setRole(role);
        userRepository.save(user);
        return true;
    }
} 