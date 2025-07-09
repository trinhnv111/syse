package com.example.syse.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "email_templates")
public class EmailTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên template không được để trống")
    @Size(min = 2, max = 100, message = "Tên template phải từ 2-100 ký tự")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Mã template không được để trống")
    @Size(min = 2, max = 100, message = "Mã template phải từ 2-100 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Mã template chỉ được chứa chữ cái, số, dấu gạch ngang và dấu gạch dưới")
    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @NotBlank(message = "Tiêu đề email không được để trống")
    @Size(min = 5, max = 255, message = "Tiêu đề email phải từ 5-255 ký tự")
    @Column(nullable = false, length = 255)
    private String subject;

    @NotBlank(message = "Nội dung email không được để trống")
    @Size(min = 10, max = 10000, message = "Nội dung email phải từ 10-10000 ký tự")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Size(max = 2000, message = "Placeholders không được vượt quá 2000 ký tự")
    @Column(columnDefinition = "json")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, String> placeholders; // Lưu JSON dạng String

    @NotNull(message = "Trạng thái không được để trống")
    @Column(nullable = false)
    private Boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Map<String, String> getPlaceholders() { return placeholders; }
    public void setPlaceholders(Map<String, String> placeholders) { this.placeholders = placeholders; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }

    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
} 