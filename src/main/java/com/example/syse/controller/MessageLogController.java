package com.example.syse.controller;

import com.example.syse.model.MessageLog;
import com.example.syse.model.User;
import com.example.syse.repository.MessageLogRepository;
import com.example.syse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/message-logs")
public class MessageLogController {
    @Autowired
    private MessageLogRepository messageLogRepository;
    @Autowired
    private UserRepository userRepository;

    // ADMIN: Xem tất cả, USER: chỉ xem của mình
    @GetMapping
    public List<MessageLog> getLogs(@RequestParam(required = false) String channel,
                                    @RequestParam(required = false) String status,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
                                    Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElse(null);
        boolean isAdmin = user != null && user.getRole() != null && "ADMIN".equals(user.getRole().getName());
        List<MessageLog> logs;
        if (isAdmin) {
            logs = messageLogRepository.findAll();
        } else {
            logs = messageLogRepository.findByUser_Id(user.getId());
        }
        // Lọc theo channel
        if (channel != null) logs = logs.stream().filter(l -> channel.equals(l.getChannel())).toList();
        // Lọc theo status
        if (status != null) logs = logs.stream().filter(l -> status.equals(l.getStatus())).toList();
        // Lọc theo thời gian
        if (from != null && to != null) logs = logs.stream().filter(l -> l.getSentAt() != null && !l.getSentAt().isBefore(from) && !l.getSentAt().isAfter(to)).toList();
        return logs;
    }
} 