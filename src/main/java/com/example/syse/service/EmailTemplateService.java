package com.example.syse.service;

import com.example.syse.dto.EmailTemplateDto;
import com.example.syse.dto.EmailTemplateDetailDto;
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
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    //Render template

    public String renderContent(String content, java.util.Map<String, String> placeholders) {
        return TemplateUtil.renderTemplate(content, placeholders);
    }

    // Lấy chi tiết template với nội dung đã render
    public EmailTemplateDetailDto getDetailWithRender(Long id, Map<String, String> placeholders) {
        EmailTemplate template = findById(id);

        // Nếu không có placeholder được truyền lên, dùng từ template
        if (placeholders == null || placeholders.isEmpty()) {
            placeholders = template.getPlaceholders();
        }

        return createDetailDto(template, placeholders);
    }


    // Lấy chi tiết template theo code với nội dung đã render
    public EmailTemplateDetailDto getDetailByCodeWithRender(String code, Map<String, String> placeholders) {
        EmailTemplate template = findByCode(code);
        return createDetailDto(template, placeholders);
    }

    // Tạo DTO với thông tin đã render
    private EmailTemplateDetailDto createDetailDto(EmailTemplate template, Map<String, String> placeholders) {
        // Render content và subject
        String renderedContent = renderContent(template.getContent(), placeholders);
        String renderedSubject = renderContent(template.getSubject(), placeholders);
        
        // Lấy danh sách placeholders có sẵn từ template
        Map<String, String> availablePlaceholders = extractPlaceholders(template.getContent() + " " + template.getSubject());
        
        return new EmailTemplateDetailDto(template, renderedContent, renderedSubject, placeholders, availablePlaceholders);
    }

    // Trích xuất các placeholder có sẵn từ content
    private Map<String, String> extractPlaceholders(String content) {
        Map<String, String> placeholders = new HashMap<>();
        Pattern pattern = Pattern.compile("\\{\\{([^}]+)\\}\\}");
        Matcher matcher = pattern.matcher(content);
        
        while (matcher.find()) {
            String placeholder = matcher.group(1).trim();
            placeholders.put(placeholder, ""); // Giá trị mặc định là rỗng
        }
        
        return placeholders;
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