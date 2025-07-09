package com.example.syse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_templates")
public class NotificationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên template không được để trống")
    @Size(min = 2, max = 100, message = "Tên template phải từ 2-100 ký tự")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Mã template không được để trống")
    @Size(min = 2, max = 100, message = "Mã template phải từ 2-100 ký tự")
    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @NotBlank(message = "Nội dung không được để trống")
    @Size(min = 10, max = 10000, message = "Nội dung phải từ 10-10000 ký tự")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Size(max = 2000, message = "Placeholders không được vượt quá 2000 ký tự")
    @Column(columnDefinition = "json")
    private String placeholders;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(String placeholders) {
        this.placeholders = placeholders;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}