package com.example.syse.dto;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailTemplateDto {
    
    @NotBlank(message = "Tên template không được để trống")
    @Size(min = 2, max = 100, message = "Tên template phải từ 2-100 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_\\u00C0-\\u017F]+$", message = "Tên template chỉ được chứa chữ cái, số, dấu cách, gạch ngang và dấu gạch dưới")
    private String name;
    
    @NotBlank(message = "Mã template không được để trống")
    @Size(min = 2, max = 100, message = "Mã template phải từ 2-100 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Mã template chỉ được chứa chữ cái, số, dấu gạch ngang và dấu gạch dưới")
    private String code;
    
    @NotBlank(message = "Tiêu đề email không được để trống")
    @Size(min = 5, max = 255, message = "Tiêu đề email phải từ 5-255 ký tự")
    private String subject;
    
    @NotBlank(message = "Nội dung email không được để trống")
    @Size(min = 10, max = 10000, message = "Nội dung email phải từ 10-10000 ký tự")
    private String content;
    
    @Size(max = 2000, message = "Placeholders không được vượt quá 2000 ký tự")
    private String placeholders;
    
    @JsonProperty("status")
    private Boolean status = true;
    
    // Constructors
    public EmailTemplateDto() {}
    
    public EmailTemplateDto(String name, String code, String subject, String content, String placeholders, Boolean status) {
        this.name = name;
        this.code = code;
        this.subject = subject;
        this.content = content;
        this.placeholders = placeholders;
        this.status = status;
    }
    
    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getPlaceholders() { return placeholders; }
    public void setPlaceholders(String placeholders) { this.placeholders = placeholders; }
    
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
} 