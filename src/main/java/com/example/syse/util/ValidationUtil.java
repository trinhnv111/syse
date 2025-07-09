package com.example.syse.util;

import com.example.syse.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class ValidationUtil {
    
    // Forbidden words
    private static final List<String> FORBIDDEN_WORDS = Arrays.asList(
        "hack", "crack", "virus", "malware", "spam", "scam", "fraud",
        "hack", "crack", "virus", "malware", "spam", "scam", "fraud",
        "đánh cắp", "lừa đảo", "gian lận", "phá hoại", "tấn công"
    );
    

    private static final List<Pattern> XSS_PATTERNS = Arrays.asList(
        Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("on\\w+\\s*=", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<iframe[^>]*>.*?</iframe>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
        Pattern.compile("<object[^>]*>.*?</object>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL),
        Pattern.compile("<embed[^>]*>.*?</embed>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL)
    );
    
    // Template variable pattern: {{variable}}
    private static final Pattern TEMPLATE_VARIABLE_PATTERN = Pattern.compile("\\{\\{([^}]+)\\}\\}");
    

    public void validateXSSProtection(String content, String fieldName) {
        if (content == null) return;
        
        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(content).find()) {
                throw new ValidationException(
                    String.format("Nội dung %s chứa mã độc hại không được phép", fieldName),
                    "XSS_DETECTED"
                );
            }
        }
    }
    

    public void validateForbiddenWords(String content, String fieldName) {
        if (content == null) return;
        
        String lowerContent = content.toLowerCase();
        for (String forbiddenWord : FORBIDDEN_WORDS) {
            if (lowerContent.contains(forbiddenWord.toLowerCase())) {
                throw new ValidationException(
                    String.format("Nội dung %s chứa từ ngữ không phù hợp: %s", fieldName, forbiddenWord),
                    "FORBIDDEN_WORD_DETECTED"
                );
            }
        }
    }
    

    public void validateTemplateSyntax(String content, String fieldName) {
        if (content == null) return;
        
        // Check for unmatched braces
        int openBraces = countOccurrences(content, "{{");
        int closeBraces = countOccurrences(content, "}}");
        
        if (openBraces != closeBraces) {
            throw new ValidationException(
                String.format("Cú pháp template trong %s không hợp lệ: số lượng {{ và }} không khớp", fieldName),
                "INVALID_TEMPLATE_SYNTAX"
            );
        }
        

        if (content.contains("{{}}")) {
            throw new ValidationException(
                String.format("Template trong %s chứa biến rỗng {{}}", fieldName),
                "EMPTY_TEMPLATE_VARIABLE"
            );
        }
    }
    

    public List<String> extractTemplateVariables(String content) {
        if (content == null) return List.of();
        
        return TEMPLATE_VARIABLE_PATTERN.matcher(content)
            .results()
            .map(matchResult -> matchResult.group(1).trim())
            .distinct()
            .toList();
    }
    

    public void validateEmailTemplateContent(String content, String fieldName) {
        validateXSSProtection(content, fieldName);
        validateForbiddenWords(content, fieldName);
        validateTemplateSyntax(content, fieldName);
    }
    

    private int countOccurrences(String text, String substring) {
        if (text == null || substring == null) return 0;
        
        int count = 0;
        int lastIndex = 0;
        while (lastIndex != -1) {
            lastIndex = text.indexOf(substring, lastIndex);
            if (lastIndex != -1) {
                count++;
                lastIndex += substring.length();
            }
        }
        return count;
    }
} 