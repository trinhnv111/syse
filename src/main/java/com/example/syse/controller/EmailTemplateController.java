package com.example.syse.controller;

import com.example.syse.model.EmailTemplate;
import com.example.syse.model.User;
import com.example.syse.service.EmailTemplateService;
import com.example.syse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
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

    // Danh sách, lọc với phân trang
    @GetMapping
    public ResponseEntity<Map<String, Object>> filter(
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Integer createdBy,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        // Validate parameters
        if (page < 0) page = 0;
        if (size < 1 || size > 100) size = 10;
        
        // Create sort
        Sort sort = sortDir.equalsIgnoreCase("asc") ?
            Sort.by(sortBy).descending() : 
            Sort.by(sortBy).ascending();
        
        // Create pageable
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Get paginated results
        Page<EmailTemplate> emailTemplatePage = emailTemplateService.filterWithPagination(
            status, createdBy, code, search, pageable);
        
        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("content", emailTemplatePage.getContent());
        response.put("currentPage", emailTemplatePage.getNumber());
        response.put("totalItems", emailTemplatePage.getTotalElements());
        response.put("totalPages", emailTemplatePage.getTotalPages());
        response.put("size", emailTemplatePage.getSize());
        response.put("hasNext", emailTemplatePage.hasNext());
        response.put("hasPrevious", emailTemplatePage.hasPrevious());
        response.put("isFirst", emailTemplatePage.isFirst());
        response.put("isLast", emailTemplatePage.isLast());
        
        return ResponseEntity.ok(response);
    }

    // ADMIN: Disable template
    @DeleteMapping("/{id}")
    public Map<String, Object> disable(@PathVariable Long id) {
        emailTemplateService.disable(id);
        return Map.of("success", true);
    }
} 