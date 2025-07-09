package com.example.syse.dto;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
    
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3-50 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Tên đăng nhập chỉ được chứa chữ cái, số, dấu gạch ngang và dấu gạch dưới")
    private String username;
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;
    
    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2-100 ký tự")
    @Pattern(regexp = "^[a-zA-Z\\s\\u00C0-\\u017F]+$", message = "Họ tên chỉ được chứa chữ cái và dấu cách")
    private String fullName;
    
    @Size(min = 6, max = 20, message = "Mật khẩu phải từ 6-20 ký tự")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]+$", 
             message = "Mật khẩu phải chứa ít nhất 1 chữ hoa, 1 chữ thường và 1 số")
    private String password;
    
    @JsonProperty("status")
    private Boolean status = true;
    
    private Long roleId;
    
    // Constructors
    public UserDto() {}
    
    public UserDto(String username, String email, String fullName, String password, Boolean status, Long roleId) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.status = status;
        this.roleId = roleId;
    }
    
    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    
    public Long getRoleId() { return roleId; }
    public void setRoleId(Long roleId) { this.roleId = roleId; }
} 