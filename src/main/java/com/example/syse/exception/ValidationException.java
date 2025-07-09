package com.example.syse.exception;

import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {
    
    private final String errorCode;
    private final Map<String, List<String>> fieldErrors;
    
    public ValidationException(String message) {
        super(message);
        this.errorCode = "VALIDATION_ERROR";
        this.fieldErrors = null;
    }
    
    public ValidationException(String message, Map<String, List<String>> fieldErrors) {
        super(message);
        this.errorCode = "VALIDATION_ERROR";
        this.fieldErrors = fieldErrors;
    }
    
    public ValidationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.fieldErrors = null;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
} 