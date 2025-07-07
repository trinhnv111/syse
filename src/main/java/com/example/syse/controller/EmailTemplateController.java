package com.example.syse.controller;

import com.example.syse.model.EmailTemplate;
import com.example.syse.model.User;
import com.example.syse.service.EmailTemplateService;
import com.example.syse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/email-templates")
public class EmailTemplateController {
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private UserRepository userRepository;

    // ADMIN: Tạo mới template
    @PostMapping
    public EmailTemplate create(@RequestBody EmailTemplate template, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        template.setCreatedBy(user);
        return emailTemplateService.create(template);
    }

    // ADMIN: Cập nhật template
    @PutMapping("/{id}")
    public EmailTemplate update(@PathVariable Long id, @RequestBody EmailTemplate template) {
        return emailTemplateService.update(id, template);
    }

    // Xem chi tiết template theo id
    @GetMapping("/{id}")
    public EmailTemplate getById(@PathVariable Long id) {
        return emailTemplateService.findById(id).orElse(null);
    }

    // Xem chi tiết template theo code
    @GetMapping("/code/{code}")
    public EmailTemplate getByCode(@PathVariable String code) {
        return emailTemplateService.findByCode(code).orElse(null);
    }

    // Danh sách, lọc
    @GetMapping
    public List<EmailTemplate> filter(@RequestParam(required = false) Boolean status,
                                      @RequestParam(required = false) Integer createdBy,
                                      @RequestParam(required = false) String code) {
        return emailTemplateService.filter(status, createdBy, code);
    }

    // ADMIN: Disable template
    @DeleteMapping("/{id}")
    public Map<String, Object> disable(@PathVariable Long id) {
        emailTemplateService.disable(id);
        return Map.of("success", true);
    }
} 