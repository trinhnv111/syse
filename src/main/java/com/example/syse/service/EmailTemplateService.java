package com.example.syse.service;

import com.example.syse.dto.EmailTemplateDto;
import com.example.syse.exception.EmailTemplateException;
import com.example.syse.exception.ResourceNotFoundException;
import com.example.syse.model.EmailTemplate;
import com.example.syse.repository.EmailTemplateRepository;
import com.example.syse.util.ValidationUtil;
import com.example.syse.util.TemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmailTemplateService {
    @Autowired
    private EmailTemplateRepository emailTemplateRepository;
    
    @Autowired
    private ValidationUtil validationUtil;

    public EmailTemplate create(EmailTemplateDto templateDto) {
        // Business validation
        validateTemplateForCreation(templateDto);
        
        // Convert DTO to entity
        EmailTemplate template = new EmailTemplate();
        template.setName(templateDto.getName());
        template.setCode(templateDto.getCode());
        template.setSubject(templateDto.getSubject());
        template.setContent(templateDto.getContent());
        template.setPlaceholders(templateDto.getPlaceholders());
        template.setStatus(templateDto.getStatus());
        
        return emailTemplateRepository.save(template);
    }

    public EmailTemplate update(Long id, EmailTemplateDto templateDto) {
        // Check if template exists
        EmailTemplate existingTemplate = emailTemplateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("EmailTemplate", "id", id));
        
        // Business validation
        validateTemplateForUpdate(templateDto, existingTemplate);
        
        // Update fields
        existingTemplate.setName(templateDto.getName());
        existingTemplate.setSubject(templateDto.getSubject());
        existingTemplate.setContent(templateDto.getContent());
        existingTemplate.setPlaceholders(templateDto.getPlaceholders());
        existingTemplate.setStatus(templateDto.getStatus());
        
        return emailTemplateRepository.save(existingTemplate);
    }

    public EmailTemplate findById(Long id) {
        return emailTemplateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("EmailTemplate", "id", id));
    }

    public EmailTemplate findByCode(String code) {
        return emailTemplateRepository.findByCode(code)
            .orElseThrow(() -> new ResourceNotFoundException("EmailTemplate", "code", code));
    }

    public Page<EmailTemplate> filterWithPagination(Boolean status, String code, String search, Pageable pageable) {
        if (status != null) {
            return emailTemplateRepository.findByStatus(status, pageable);
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
        EmailTemplate template = emailTemplateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("EmailTemplate", "id", id));
        template.setStatus(false);
        emailTemplateRepository.save(template);
    }
    
    /**
     * Render template content by replacing {{key}} with value from placeholders.
     */
    public String renderContent(String content, java.util.Map<String, String> placeholders) {
        return TemplateUtil.renderTemplate(content, placeholders);
    }
   
    private void validateTemplateForCreation(EmailTemplateDto templateDto) {
        // Check if code 
        if (emailTemplateRepository.findByCode(templateDto.getCode()).isPresent()) {
            throw new EmailTemplateException("Mã template đã tồn tại: " + templateDto.getCode(), "DUPLICATE_CODE");
        }
        
        // Validate 
        validationUtil.validateEmailTemplateContent(templateDto.getContent(), "content");
        validationUtil.validateEmailTemplateContent(templateDto.getSubject(), "subject");
    }
    
    private void validateTemplateForUpdate(EmailTemplateDto templateDto, EmailTemplate existingTemplate) {
        // Validate content
        validationUtil.validateEmailTemplateContent(templateDto.getContent(), "content");
        validationUtil.validateEmailTemplateContent(templateDto.getSubject(), "subject");
        
        // Check if name is being changed and if it conflicts with existing
        if (!existingTemplate.getName().equals(templateDto.getName())) {
            
        }
    }
} 