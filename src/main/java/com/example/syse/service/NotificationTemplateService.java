package com.example.syse.service;

import com.example.syse.dto.NotificationTemplateDto;
import com.example.syse.exception.ResourceNotFoundException;
import com.example.syse.exception.EmailTemplateException;
import com.example.syse.model.NotificationTemplate;
import com.example.syse.repository.NotificationTemplateRepository;
import com.example.syse.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@Transactional
public class NotificationTemplateService {
    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;
    @Autowired
    private ValidationUtil validationUtil;

    public NotificationTemplate create(NotificationTemplateDto dto) {
        validateTemplateForCreation(dto);
        NotificationTemplate template = new NotificationTemplate();
        template.setName(dto.getName());
        template.setCode(dto.getCode());
        template.setContent(dto.getContent());
        template.setPlaceholders(dto.getPlaceholders());
        template.setStatus(dto.getStatus());
        return notificationTemplateRepository.save(template);
    }

    public NotificationTemplate update(Long id, NotificationTemplateDto dto) {
        NotificationTemplate existing = notificationTemplateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate", "id", id));
        validateTemplateForUpdate(dto, existing);
        existing.setName(dto.getName());
        existing.setContent(dto.getContent());
        existing.setPlaceholders(dto.getPlaceholders());
        existing.setStatus(dto.getStatus());
        return notificationTemplateRepository.save(existing);
    }

    public NotificationTemplate findById(Long id) {
        return notificationTemplateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate", "id", id));
    }

    public NotificationTemplate findByCode(String code) {
        return notificationTemplateRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate", "code", code));
    }

    public List<NotificationTemplate> findAll() {
        return notificationTemplateRepository.findAll();
    }

    public Page<NotificationTemplate> filterWithPagination(Boolean status, String code, String search, Pageable pageable) {
        if (status != null) {
            return notificationTemplateRepository.findByStatus(status, pageable);
        }
        if (code != null) {
            return notificationTemplateRepository.findByCodeContaining(code, pageable);
        }
        if (search != null && !search.trim().isEmpty()) {
            return notificationTemplateRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCaseOrContentContainingIgnoreCase(
                search.trim(), search.trim(), search.trim(), pageable);
        }
        return notificationTemplateRepository.findAll(pageable);
    }

    public void disable(Long id) {
        NotificationTemplate template = notificationTemplateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("NotificationTemplate", "id", id));
        template.setStatus(false);
        notificationTemplateRepository.save(template);
    }

    // Business validation
    private void validateTemplateForCreation(NotificationTemplateDto dto) {
        if (notificationTemplateRepository.findByCode(dto.getCode()).isPresent()) {
            throw new EmailTemplateException("Mã template đã tồn tại: " + dto.getCode(), "DUPLICATE_CODE");
        }
        validationUtil.validateEmailTemplateContent(dto.getContent(), "content");
    }
    private void validateTemplateForUpdate(NotificationTemplateDto dto, NotificationTemplate existing) {
        validationUtil.validateEmailTemplateContent(dto.getContent(), "content");
    }
} 