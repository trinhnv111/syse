package com.example.syse.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    private final String errorCode;
    
    public ResourceNotFoundException(String message) {
        super(message);
        this.errorCode = "RESOURCE_NOT_FOUND";
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s không tìm thấy với %s : '%s'", resourceName, fieldName, fieldValue));
        this.errorCode = "RESOURCE_NOT_FOUND";
    }
    
    public String getErrorCode() {
        return errorCode;
    }
} 