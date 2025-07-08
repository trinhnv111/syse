package com.example.syse.dto;

import java.time.LocalDateTime;

public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private String roleName;
    private Boolean enabled;
    private LocalDateTime createdAt;

    // Constructors
    public UserDto() {}

    public UserDto(Integer id, String username, String email, String fullName, String roleName, Boolean enabled, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.roleName = roleName;
        this.enabled = enabled;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 