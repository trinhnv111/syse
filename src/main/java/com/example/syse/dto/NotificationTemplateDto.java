package com.example.syse.dto;

import jakarta.validation.constraints.*;

public class NotificationTemplateDto {
    @NotBlank(message = "Tên template không được để trống")
    @Size(min = 2, max = 100, message = "Tên template phải từ 2-100 ký tự")
    private String name;

    @NotBlank(message = "Mã template không được để trống")
    @Size(min = 2, max = 100, message = "Mã template phải từ 2-100 ký tự")
    private String code;

    @NotBlank(message = "Nội dung không được để trống")
    @Size(min = 10, max = 10000, message = "Nội dung phải từ 10-10000 ký tự")
    private String content;

    @Size(max = 2000, message = "Placeholders không được vượt quá 2000 ký tự")
    private String placeholders;

    private Boolean status = true;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getPlaceholders() { return placeholders; }
    public void setPlaceholders(String placeholders) { this.placeholders = placeholders; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
} 