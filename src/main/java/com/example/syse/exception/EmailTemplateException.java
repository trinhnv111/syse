package com.example.syse.exception;

public class EmailTemplateException extends RuntimeException {
    
    private final String errorCode;
    
    public EmailTemplateException(String message) {
        super(message);
        this.errorCode = "EMAIL_TEMPLATE_ERROR";
    }
    
    public EmailTemplateException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public EmailTemplateException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "EMAIL_TEMPLATE_ERROR";
    }
    
    public EmailTemplateException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
} 