package com.example.syse.dto;

import com.example.syse.model.EmailTemplate;
import java.util.Map;

public class EmailTemplateDetailDto {
    private EmailTemplate template;
    private String renderedContent;
    private String renderedSubject;
    private Map<String, String> placeholders;
    private Map<String, String> availablePlaceholders;

    public EmailTemplateDetailDto() {}

    public EmailTemplateDetailDto(EmailTemplate template, String renderedContent, String renderedSubject, 
                                Map<String, String> placeholders, Map<String, String> availablePlaceholders) {
        this.template = template;
        this.renderedContent = renderedContent;
        this.renderedSubject = renderedSubject;
        this.placeholders = placeholders;
        this.availablePlaceholders = availablePlaceholders;
    }

    // Getters and Setters
    public EmailTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EmailTemplate template) {
        this.template = template;
    }

    public String getRenderedContent() {
        return renderedContent;
    }

    public void setRenderedContent(String renderedContent) {
        this.renderedContent = renderedContent;
    }

    public String getRenderedSubject() {
        return renderedSubject;
    }

    public void setRenderedSubject(String renderedSubject) {
        this.renderedSubject = renderedSubject;
    }

    public Map<String, String> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(Map<String, String> placeholders) {
        this.placeholders = placeholders;
    }

    public Map<String, String> getAvailablePlaceholders() {
        return availablePlaceholders;
    }

    public void setAvailablePlaceholders(Map<String, String> availablePlaceholders) {
        this.availablePlaceholders = availablePlaceholders;
    }
} 