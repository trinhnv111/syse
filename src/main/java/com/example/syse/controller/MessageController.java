package com.example.syse.controller;

import com.example.syse.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    // Gửi email động
    @PostMapping("/send-email")
    public Map<String, Object> sendEmail(@RequestBody Map<String, Object> req) {
        String templateCode = (String) req.get("templateCode");
        Integer userId = (Integer) req.get("userId");
        Map<String, String> placeholders = (Map<String, String>) req.get("placeholders");
        boolean sent = messageService.sendEmail(templateCode, userId, placeholders);
        return Map.of("success", sent);
    }

    // Gửi notification động
    @PostMapping("/send-notification")
    public Map<String, Object> sendNotification(@RequestBody Map<String, Object> req) {
        String templateCode = (String) req.get("templateCode");
        Integer userId = (Integer) req.get("userId");
        Map<String, String> placeholders = (Map<String, String>) req.get("placeholders");
        boolean sent = messageService.sendNotification(templateCode, userId, placeholders);
        return Map.of("success", sent);
    }
} 