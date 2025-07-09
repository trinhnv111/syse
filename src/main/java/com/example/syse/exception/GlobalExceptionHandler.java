package com.example.syse.exception;

import com.example.syse.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(EmailTemplateException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailTemplateException(
            EmailTemplateException ex, WebRequest request) {
        ApiResponse<Void> response = ApiResponse.error(ex.getMessage(), ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidationException(
            ValidationException ex, WebRequest request) {
        Map<String, Object> errorData = new HashMap<>();
        if (ex.getFieldErrors() != null) {
            errorData.put("fieldErrors", ex.getFieldErrors());
        }
        
        ApiResponse<Map<String, Object>> response = new ApiResponse<>(false, ex.getMessage(), errorData);
        response.setErrorCode(ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        // Lấy danh sách field từ BindingResult
                List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();

                for (FieldError fieldError : fieldErrorList) {
                    String fieldName = fieldError.getField();
                    String errorMessage = fieldError.getDefaultMessage();
                    if (fieldErrors.containsKey(fieldName)) {
                        String existingMessage = fieldErrors.get(fieldName);
                        String combinedMessage = existingMessage + "; " + errorMessage;
                        fieldErrors.put(fieldName, combinedMessage);
                    } else {
                        fieldErrors.put(fieldName, errorMessage);
                    }
                }

        Map<String, Object> errorData = new HashMap<>();
        errorData.put("fieldErrors", fieldErrors);

        ApiResponse<Map<String, Object>> response = new ApiResponse<>(false, "Dữ liệu không hợp lệ", errorData);
        response.setErrorCode("VALIDATION_ERROR");
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(
            Exception ex, WebRequest request) {
        ApiResponse<Void> response = ApiResponse.error("Đã xảy ra lỗi hệ thống", "INTERNAL_SERVER_ERROR");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 