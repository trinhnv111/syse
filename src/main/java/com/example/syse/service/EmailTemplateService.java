package com.example.syse.service;

import com.example.syse.model.EmailTemplate;
import com.example.syse.repository.EmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmailTemplateService {
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    public EmailTemplate create(EmailTemplate template) {
        return emailTemplateRepository.save(template);
    }

    public EmailTemplate update(Long id, EmailTemplate template) {
        template.setId(id);
        return emailTemplateRepository.save(template);
    }

    public Optional<EmailTemplate> findById(Long id) {
        return emailTemplateRepository.findById(id);
    }

    public Optional<EmailTemplate> findByCode(String code) {
        return emailTemplateRepository.findByCode(code);
    }

    public List<EmailTemplate> findAll() {
        return emailTemplateRepository.findAll();
    }

    public List<EmailTemplate> filter(Boolean status, Integer createdById, String code) {
        if (status != null) return emailTemplateRepository.findByStatus(status);
        if (createdById != null) return emailTemplateRepository.findByCreatedBy_Id(createdById);
        if (code != null) return emailTemplateRepository.findByCodeContaining(code);
        return emailTemplateRepository.findAll();
    }

    public Page<EmailTemplate> filterWithPagination(Boolean status, Integer createdById, String code, String search, Pageable pageable) {
        if (status != null) {
            return emailTemplateRepository.findByStatus(status, pageable);
        }
        if (createdById != null) {
            return emailTemplateRepository.findByCreatedBy_Id(createdById, pageable);
        }
        if (code != null) {
            return emailTemplateRepository.findByCodeContaining(code, pageable);
        }
        if (search != null && !search.trim().isEmpty()) {
            return emailTemplateRepository.findByNameContainingIgnoreCaseOrCodeContainingIgnoreCaseOrSubjectContainingIgnoreCase(
                search.trim(), search.trim(), search.trim(), pageable);
        }
        return emailTemplateRepository.findAll(pageable);
    }

    public void disable(Long id) {
        emailTemplateRepository.findById(id).ifPresent(t -> {
            t.setStatus(false);
            emailTemplateRepository.save(t);
        });
    }
} 