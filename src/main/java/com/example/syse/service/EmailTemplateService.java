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

    // Trích các placeholder có sẵn từ content
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
        // Validate content và subject
        validationUtil.validateEmailTemplateContent(templateDto.getContent(), "content");
        validationUtil.validateEmailTemplateContent(templateDto.getSubject(), "subject");

        // Kiểm tra nếu name đang được thay đổi và có xung đột với template khác
        if (!existingTemplate.getName().equals(templateDto.getName())) {
            // Kiểm tra xem có template nào khác đã có tên này chưa
            Optional<EmailTemplate> templateWithSameName = emailTemplateRepository.findByName(templateDto.getName());
            if (templateWithSameName.isPresent() && !templateWithSameName.get().getId().equals(existingTemplate.getId())) {
                throw new EmailTemplateException("Tên template đã tồn tại: " + templateDto.getName(), "DUPLICATE_NAME");
            }
        }

        // Kiểm tra nếu mã code đang được thay đổi
        if (!existingTemplate.getCode().equals(templateDto.getCode())) {
            // Kiểm tra xem có template nào khác đã có code này chưa
            Optional<EmailTemplate> templateWithSameCode = emailTemplateRepository.findByCode(templateDto.getCode());
            if (templateWithSameCode.isPresent() && !templateWithSameCode.get().getId().equals(existingTemplate.getId())) {
                throw new EmailTemplateException("Mã template đã tồn tại: " + templateDto.getCode(), "DUPLICATE_CODE");
            }
        }

        // Validate placeholders nếu có
        if (templateDto.getPlaceholders() != null) {
            validatePlaceholders(templateDto.getPlaceholders());
        }

        // Kiểm tra xem template có đang được sử dụng không (nếu cần)
        // Có thể thêm logic kiểm tra template có đang được sử dụng trong các email đã gửi không
        validateTemplateUsage(existingTemplate);
    }

    private void validatePlaceholders(Map<String, String> placeholders) {
        if (placeholders != null) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                
                // Kiểm tra key không được rỗng
                if (key == null || key.trim().isEmpty()) {
                    throw new EmailTemplateException("Placeholder key không được để trống", "INVALID_PLACEHOLDER_KEY");
                }
                
                // Kiểm tra key chỉ chứa ký tự hợp lệ
                if (!key.matches("^[a-zA-Z0-9_]+$")) {
                    throw new EmailTemplateException("Placeholder key chỉ được chứa chữ cái, số và dấu gạch dưới: " + key, "INVALID_PLACEHOLDER_KEY");
                }
                
                // Kiểm tra độ dài key
                if (key.length() > 50) {
                    throw new EmailTemplateException("Placeholder key không được vượt quá 50 ký tự: " + key, "INVALID_PLACEHOLDER_KEY");
                }
            }
        }
    }

    private void validateTemplateUsage(EmailTemplate template) {
        // kiểm tra xem template có đang được sử dụng không

    }
} 