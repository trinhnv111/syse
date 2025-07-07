package com.example.syse.service;

import com.example.syse.model.NotificationTemplate;
import com.example.syse.repository.NotificationTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationTemplateService {
    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    public NotificationTemplate create(NotificationTemplate template) {
        return notificationTemplateRepository.save(template);
    }

    public NotificationTemplate update(Long id, NotificationTemplate template) {
        template.setId(id);
        return notificationTemplateRepository.save(template);
    }

    public Optional<NotificationTemplate> findById(Long id) {
        return notificationTemplateRepository.findById(id);
    }

    public Optional<NotificationTemplate> findByCode(String code) {
        return notificationTemplateRepository.findByCode(code);
    }

    public List<NotificationTemplate> findAll() {
        return notificationTemplateRepository.findAll();
    }

    public List<NotificationTemplate> filter(Boolean status, Integer createdById, String code) {
        if (status != null) return notificationTemplateRepository.findByStatus(status);
        if (createdById != null) return notificationTemplateRepository.findByCreatedBy_Id(createdById);
        if (code != null) return notificationTemplateRepository.findByCodeContaining(code);
        return notificationTemplateRepository.findAll();
    }

    public void disable(Long id) {
        notificationTemplateRepository.findById(id).ifPresent(t -> {
            t.setStatus(false);
            notificationTemplateRepository.save(t);
        });
    }
} 