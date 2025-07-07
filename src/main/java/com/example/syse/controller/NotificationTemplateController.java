package com.example.syse.controller;

import com.example.syse.model.NotificationTemplate;
import com.example.syse.model.User;
import com.example.syse.service.NotificationTemplateService;
import com.example.syse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification-templates")
public class NotificationTemplateController {
    @Autowired
    private NotificationTemplateService notificationTemplateService;
    @Autowired
    private UserRepository userRepository;

    // ADMIN: Tạo mới template
    @PostMapping
    public NotificationTemplate create(@RequestBody NotificationTemplate template, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        template.setCreatedBy(user);
        return notificationTemplateService.create(template);
    }

    // ADMIN: Cập nhật template
    @PutMapping("/{id}")
    public NotificationTemplate update(@PathVariable Long id, @RequestBody NotificationTemplate template) {
        return notificationTemplateService.update(id, template);
    }

    // Xem chi tiết template theo id
    @GetMapping("/{id}")
    public NotificationTemplate getById(@PathVariable Long id) {
        return notificationTemplateService.findById(id).orElse(null);
    }

    // Xem chi tiết template theo code
    @GetMapping("/code/{code}")
    public NotificationTemplate getByCode(@PathVariable String code) {
        return notificationTemplateService.findByCode(code).orElse(null);
    }

    // Danh sách, lọc
    @GetMapping
    public List<NotificationTemplate> filter(@RequestParam(required = false) Boolean status,
                                             @RequestParam(required = false) Integer createdBy,
                                             @RequestParam(required = false) String code) {
        return notificationTemplateService.filter(status, createdBy, code);
    }

    // ADMIN: Disable template
    @DeleteMapping("/{id}")
    public Map<String, Object> disable(@PathVariable Long id) {
        notificationTemplateService.disable(id);
        return Map.of("success", true);
    }
} 