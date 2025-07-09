package com.example.syse.controller;

import com.example.syse.util.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/data-generator")
@CrossOrigin(origins = "*")
public class DataGeneratorController {

    @Autowired
    private DataGenerator dataGenerator;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateData(
            @RequestParam(defaultValue = "100") int userCount,
            @RequestParam(defaultValue = "50") int emailTemplateCount,
            @RequestParam(defaultValue = "30") int notificationTemplateCount,
            @RequestParam(defaultValue = "200") int messageLogCount) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Generate data
            dataGenerator.generateData();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            response.put("success", true);
            response.put("message", "Dữ liệu đã được tạo thành công!");
            response.put("generatedData", Map.of(
                "users", userCount,
                "emailTemplates", emailTemplateCount,
                "notificationTemplates", notificationTemplateCount,
                "messageLogs", messageLogCount
            ));
            response.put("duration", duration + "ms");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo dữ liệu: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/generate-email-templates")
    public ResponseEntity<Map<String, Object>> generateEmailTemplatesOnly(
            @RequestParam(defaultValue = "50") int count) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Generate only email templates
            dataGenerator.generateEmailTemplates(count);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            response.put("success", true);
            response.put("message", "Đã tạo " + count + " email templates thành công!");
            response.put("count", count);
            response.put("duration", duration + "ms");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo email templates: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/generate-users")
    public ResponseEntity<Map<String, Object>> generateUsersOnly(
            @RequestParam(defaultValue = "100") int count) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Generate only users
            dataGenerator.generateUsers(count);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            response.put("success", true);
            response.put("message", "Đã tạo " + count + " users thành công!");
            response.put("count", count);
            response.put("duration", duration + "ms");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo users: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/generate-notification-templates")
    public ResponseEntity<Map<String, Object>> generateNotificationTemplatesOnly(
            @RequestParam(defaultValue = "30") int count) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Generate only notification templates
            dataGenerator.generateNotificationTemplates(count);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            response.put("success", true);
            response.put("message", "Đã tạo " + count + " notification templates thành công!");
            response.put("count", count);
            response.put("duration", duration + "ms");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo notification templates: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/generate-message-logs")
    public ResponseEntity<Map<String, Object>> generateMessageLogsOnly(
            @RequestParam(defaultValue = "200") int count) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            long startTime = System.currentTimeMillis();
            
            // Generate only message logs
            dataGenerator.generateMessageLogs(count);
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            response.put("success", true);
            response.put("message", "Đã tạo " + count + " message logs thành công!");
            response.put("count", count);
            response.put("duration", duration + "ms");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo message logs: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getDataStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            long userCount = dataGenerator.getUserRepository().count();
            long emailTemplateCount = dataGenerator.getEmailTemplateRepository().count();
            long notificationTemplateCount = dataGenerator.getNotificationTemplateRepository().count();
            long messageLogCount = dataGenerator.getMessageLogRepository().count();
            long roleCount = dataGenerator.getRoleRepository().count();
            
            response.put("success", true);
            response.put("data", Map.of(
                "users", userCount,
                "emailTemplates", emailTemplateCount,
                "notificationTemplates", notificationTemplateCount,
                "messageLogs", messageLogCount,
                "roles", roleCount
            ));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi lấy thông tin dữ liệu: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
} 