package com.example.syse.controller;

import com.example.syse.dto.ApiResponse;
import com.example.syse.dto.NotificationTemplateDto;
import com.example.syse.model.NotificationTemplate;
import com.example.syse.model.User;
import com.example.syse.service.NotificationTemplateService;
import com.example.syse.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/notification-templates")
public class NotificationTemplateController {
    @Autowired
    private NotificationTemplateService notificationTemplateService;
    @Autowired
    private UserRepository userRepository;

    // ADMIN: Tạo mới template
    @PostMapping
    public ResponseEntity<ApiResponse<NotificationTemplate>> create(
            @Valid @RequestBody NotificationTemplateDto dto,
            Authentication auth) {
        try {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            NotificationTemplate template = notificationTemplateService.create(dto);
            template.setCreatedBy(user);
            ApiResponse<NotificationTemplate> response = ApiResponse.success("Tạo template thành công", template);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ApiResponse<NotificationTemplate> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // ADMIN: Cập nhật template
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationTemplate>> update(
            @PathVariable Long id,
            @Valid @RequestBody NotificationTemplateDto dto) {
        try {
            NotificationTemplate template = notificationTemplateService.update(id, dto);
            ApiResponse<NotificationTemplate> response = ApiResponse.success("Cập nhật template thành công", template);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<NotificationTemplate> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Xem chi tiết template theo id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationTemplate>> getById(@PathVariable Long id) {
        try {
            NotificationTemplate template = notificationTemplateService.findById(id);
            ApiResponse<NotificationTemplate> response = ApiResponse.success("Lấy thông tin template thành công", template);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<NotificationTemplate> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Xem chi tiết template theo code
    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<NotificationTemplate>> getByCode(@PathVariable String code) {
        try {
            NotificationTemplate template = notificationTemplateService.findByCode(code);
            ApiResponse<NotificationTemplate> response = ApiResponse.success("Lấy thông tin template thành công", template);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<NotificationTemplate> response = ApiResponse.error(e.getMessage());
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
            if (page < 0) page = 0;
            if (size < 1 || size > 100) size = 10;
            Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<NotificationTemplate> notificationTemplatePage = notificationTemplateService.filterWithPagination(
                status, code, search, pageable);
            Map<String, Object> paginationData = new HashMap<>();
            paginationData.put("content", notificationTemplatePage.getContent());
            paginationData.put("currentPage", notificationTemplatePage.getNumber());
            paginationData.put("totalItems", notificationTemplatePage.getTotalElements());
            paginationData.put("totalPages", notificationTemplatePage.getTotalPages());
            paginationData.put("size", notificationTemplatePage.getSize());
            paginationData.put("hasNext", notificationTemplatePage.hasNext());
            paginationData.put("hasPrevious", notificationTemplatePage.hasPrevious());
            paginationData.put("isFirst", notificationTemplatePage.isFirst());
            paginationData.put("isLast", notificationTemplatePage.isLast());
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
            notificationTemplateService.disable(id);
            ApiResponse<Void> response = ApiResponse.success("Vô hiệu hóa template thành công");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = ApiResponse.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
} 