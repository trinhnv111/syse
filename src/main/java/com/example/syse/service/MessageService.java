package com.example.syse.service;

import com.example.syse.model.*;
import com.example.syse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class MessageService {
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageLogRepository messageLogRepository;
    @Autowired(required = false)
    private JavaMailSender mailSender;

    // Render template với placeholders
    private String render(String content, Map<String, String> placeholders) {
        String result = content;
        if (placeholders != null) {
            for (var entry : placeholders.entrySet()) {
                result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }
        return result;
    }

    // Gửi email
    public boolean sendEmail(String templateCode, Integer userId, Map<String, String> placeholders) {
        EmailTemplate template = emailTemplateRepository.findByCode(templateCode).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (template == null || user == null) return false;
        String renderedSubject = render(template.getSubject(), placeholders);
        String renderedContent = render(template.getContent(), placeholders);
        boolean sent = false;
        String status = "FAILED";
        try {
            if (mailSender != null) {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(user.getEmail());
                helper.setSubject(renderedSubject);
                helper.setText(renderedContent, true);
                mailSender.send(message);
                sent = true;
                status = "SENT";
            }
        } catch (MessagingException e) {
            status = "FAILED";
        }
        // Ghi log
        MessageLog log = new MessageLog();
        log.setUser(user);
        log.setChannel("EMAIL");
        log.setTemplateCode(templateCode);
        log.setContent(renderedContent);
        log.setStatus(status);
        messageLogRepository.save(log);
        return sent;
    }

    // Gửi notification (giả lập)
    public boolean sendNotification(String templateCode, Integer userId, Map<String, String> placeholders) {
        NotificationTemplate template = notificationTemplateRepository.findByCode(templateCode).orElse(null);
        User user = userRepository.findById(userId).orElse(null);
        if (template == null || user == null) return false;
        String renderedContent = render(template.getContent(), placeholders);
        // TODO: Đẩy notification thực tế nếu có
        String status = "SENT";
        // Ghi log
        MessageLog log = new MessageLog();
        log.setUser(user);
        log.setChannel("NOTIFICATION");
        log.setTemplateCode(templateCode);
        log.setContent(renderedContent);
        log.setStatus(status);
        messageLogRepository.save(log);
        return true;
    }
} 