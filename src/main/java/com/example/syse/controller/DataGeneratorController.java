package com.example.syse.controller;

import com.example.syse.util.DataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-generator")
public class DataGeneratorController {
    @Autowired
    private DataGenerator dataGenerator;

    @PostMapping("/generate-email-templates")
    public ResponseEntity<?> generateEmailTemplates(@RequestParam int count) {
        dataGenerator.generateEmailTemplates(count);
        return ResponseEntity.ok("Đã tạo email templates mẫu");
    }

    @PostMapping("/generate-notification-templates")
    public ResponseEntity<?> generateNotificationTemplates(@RequestParam int count) {
        dataGenerator.generateNotificationTemplates(count);
        return ResponseEntity.ok("Đã tạo notification templates mẫu");
    }

    @PostMapping("/generate-users")
    public ResponseEntity<?> generateUsers(@RequestParam int count) {
        dataGenerator.generateUsers(count);
        return ResponseEntity.ok("Đã tạo users mẫu");
    }

    @PostMapping("/generate-roles")
    public ResponseEntity<?> generateRoles() {
        dataGenerator.generateRoles();
        return ResponseEntity.ok("Đã tạo roles mẫu");
    }

    @PostMapping("/generate-message-logs")
    public ResponseEntity<?> generateMessageLogs(@RequestParam int count) {
        dataGenerator.generateMessageLogs(count);
        return ResponseEntity.ok("Đã tạo message logs mẫu");
    }
} 