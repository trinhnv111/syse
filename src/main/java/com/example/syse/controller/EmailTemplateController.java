package com.example.syse.controller;

import com.example.syse.dto.ApiResponse;
import com.example.syse.dto.EmailTemplateDto;
import com.example.syse.model.EmailTemplate;
import com.example.syse.model.User;
import com.example.syse.service.EmailTemplateService;
import com.example.syse.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
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
    public ResponseEntity<ApiResponse<EmailTemplate>> create(
            @Valid @RequestBody EmailTemplateDto templateDto, 
            Authentication auth) {
        try {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            EmailTemplate template = emailTemplateService.create(templateDto);
            template.setCreatedBy(user);
            
            ApiResponse<EmailTemplate> response = ApiResponse.success("Tạo template thành công", template);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<EmailTemplate> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // ADMIN: Cập nhật template
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmailTemplate>> update(
            @PathVariable Long id, 
            @Valid @RequestBody EmailTemplateDto templateDto) {
        try {
            EmailTemplate template = emailTemplateService.update(id, templateDto);
            ApiResponse<EmailTemplate> response = ApiResponse.success("Cập nhật template thành công", template);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<EmailTemplate> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Xem chi tiết template theo id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmailTemplate>> getById(@PathVariable Long id) {
        try {
            EmailTemplate template = emailTemplateService.findById(id);
            ApiResponse<EmailTemplate> response = ApiResponse.success("Lấy thông tin template thành công", template);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<EmailTemplate> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Xem chi tiết template theo code
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<EmailTemplate>> getByCode(@PathVariable String code) {
        try {
            EmailTemplate template = emailTemplateService.findByCode(code);
            ApiResponse<EmailTemplate> response = ApiResponse.success("Lấy thông tin template thành công", template);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<EmailTemplate> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Danh sách, lọc với phân trang
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> filter(
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        try {
            // Validate parameters
            if (page < 0) page = 0;
            if (size < 1 || size > 100) size = 10;
            
            // Create sort
            Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() : 
                Sort.by(sortBy).descending();
            
            // Create pageable
            Pageable pageable = PageRequest.of(page, size, sort);
            
            // Get paginated results
            Page<EmailTemplate> emailTemplatePage = emailTemplateService.filterWithPagination(
                status, code, search, pageable);
            
            // Build response
            Map<String, Object> paginationData = new HashMap<>();
            paginationData.put("content", emailTemplatePage.getContent());
            paginationData.put("currentPage", emailTemplatePage.getNumber());
            paginationData.put("totalItems", emailTemplatePage.getTotalElements());
            paginationData.put("totalPages", emailTemplatePage.getTotalPages());
            paginationData.put("size", emailTemplatePage.getSize());
            paginationData.put("hasNext", emailTemplatePage.hasNext());
            paginationData.put("hasPrevious", emailTemplatePage.hasPrevious());
            paginationData.put("isFirst", emailTemplatePage.isFirst());
            paginationData.put("isLast", emailTemplatePage.isLast());
            
            ApiResponse<Map<String, Object>> response = ApiResponse.success("Lấy danh sách template thành công", paginationData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Map<String, Object>> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // ADMIN: Disable template
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> disable(@PathVariable Long id) {
        try {
            emailTemplateService.disable(id);
            ApiResponse<Void> response = ApiResponse.success("Vô hiệu hóa template thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // TEST: Render template with placeholders
    @PostMapping("/test-render")
    public ResponseEntity<ApiResponse<String>> testRender(@RequestBody Map<String, Object> payload) {
        try {
            String content = (String) payload.get("content");
            Map<String, String> placeholders = (Map<String, String>) payload.get("placeholders");
            String rendered = emailTemplateService.renderContent(content, placeholders);
            ApiResponse<String> response = ApiResponse.success("Render thành công", rendered);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
} 