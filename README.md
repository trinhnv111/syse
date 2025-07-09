# SYSE - System for Email/Notification Management

## 🎯 Tổng quan

SYSE là hệ thống quản lý template email và thông báo với đầy đủ validation và best practices.

## 🏗️ Kiến trúc Clean Architecture

```
src/main/java/com/example/syse/
├── controller/     # REST API Controllers
├── service/        # Business Logic Layer
├── repository/     # Data Access Layer
├── model/          # Entity Classes
├── dto/            # Data Transfer Objects
├── exception/      # Custom Exceptions
├── security/       # Security Configuration
└── util/           # Utility Classes
```

## 🔒 Validation & Security Features

### 1. Validation Nhiều Tầng

#### Entity Level Validation
```java
@NotBlank(message = "Tên template không được để trống")
@Size(min = 2, max = 100, message = "Tên template phải từ 2-100 ký tự")
@Pattern(regexp = "^[a-zA-Z0-9\\s\\-_\\u00C0-\\u017F]+$")
private String name;
```

#### DTO Level Validation
```java
@Valid @RequestBody EmailTemplateDto templateDto
```

#### Business Level Validation
- Kiểm tra duplicate code
- Validate template syntax
- XSS protection
- Forbidden words detection

### 2. Security Features

#### XSS Protection
```java
// Chặn các pattern nguy hiểm
Pattern.compile("<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE)
Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE)
Pattern.compile("on\\w+\\s*=", Pattern.CASE_INSENSITIVE)
```

#### Template Syntax Validation
```java
// Kiểm tra cú pháp {{variable}}
Pattern.compile("\\{\\{([^}]+)\\}\\}")
```

#### Forbidden Words Filter
```java
private static final List<String> FORBIDDEN_WORDS = Arrays.asList(
    "hack", "crack", "virus", "malware", "spam", "scam", "fraud",
    "đánh cắp", "lừa đảo", "gian lận", "phá hoại", "tấn công"
);
```

## 📡 API Endpoints

### Email Template Management

#### Tạo template mới
```http
POST /api/email-templates
Content-Type: application/json

{
  "name": "Template chào mừng",
  "code": "WELCOME_EMAIL",
  "subject": "Chào mừng bạn đến với hệ thống",
  "content": "Xin chào {{userName}}, chào mừng bạn đến với {{systemName}}!",
  "placeholders": "[\"userName\", \"systemName\"]",
  "status": true
}
```

#### Cập nhật template
```http
PUT /api/email-templates/{id}
Content-Type: application/json

{
  "name": "Template chào mừng cập nhật",
  "subject": "Chào mừng bạn đến với hệ thống mới",
  "content": "Xin chào {{userName}}, chào mừng bạn đến với {{systemName}}!",
  "status": true
}
```

#### Lấy danh sách template
```http
GET /api/email-templates?page=0&size=10&sortBy=name&sortDir=asc
```

#### Lấy template theo ID
```http
GET /api/email-templates/{id}
```

#### Lấy template theo code
```http
GET /api/email-templates/code/{code}
```

#### Vô hiệu hóa template
```http
DELETE /api/email-templates/{id}
```

## 📋 Response Format

### Success Response
```json
{
  "success": true,
  "message": "Tạo template thành công",
  "data": {
    "id": 1,
    "name": "Template chào mừng",
    "code": "WELCOME_EMAIL",
    "subject": "Chào mừng bạn đến với hệ thống",
    "content": "Xin chào {{userName}}, chào mừng bạn đến với {{systemName}}!",
    "status": true,
    "createdAt": "2024-01-15T10:30:00"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### Error Response
```json
{
  "success": false,
  "message": "Dữ liệu không hợp lệ",
  "errorCode": "VALIDATION_ERROR",
  "data": {
    "fieldErrors": {
      "name": "Tên template không được để trống",
      "code": "Mã template đã tồn tại"
    }
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

## 🚀 Cách sử dụng

### 1. Khởi chạy ứng dụng
```bash
mvn spring-boot:run
```

### 2. Test API với curl

#### Tạo template
```bash
curl -X POST http://localhost:8080/api/email-templates \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Template test",
    "code": "TEST_EMAIL",
    "subject": "Email test",
    "content": "Xin chào {{userName}}!"
  }'
```

#### Lấy danh sách
```bash
curl -X GET "http://localhost:8080/api/email-templates?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🔧 Configuration

### Database Configuration
```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/syse
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

### JWT Configuration
```properties
jwt.secret=your-secret-key
jwt.expiration=86400000
```

## 🧪 Testing

### Unit Tests
```bash
mvn test
```

### Integration Tests
```bash
mvn test -Dtest=*IntegrationTest
```

## 📝 Best Practices Implemented

### 1. Clean Architecture
- **Controller**: Chỉ xử lý HTTP requests/responses
- **Service**: Chứa business logic
- **Repository**: Chỉ data access
- **DTO**: Tách input/output khỏi entity

### 2. Validation Strategy
- **Entity Level**: @NotBlank, @Size, @Pattern
- **DTO Level**: @Valid annotation
- **Business Level**: Custom validation logic

### 3. Error Handling
- **Custom Exceptions**: EmailTemplateException, ResourceNotFoundException
- **Global Exception Handler**: Xử lý tất cả exceptions
- **Standardized Response**: Format response thống nhất

### 4. Security
- **XSS Protection**: Chặn script tags
- **Input Validation**: Kiểm tra input format
- **JWT Authentication**: Secure API access

### 5. Code Quality
- **Transaction Management**: @Transactional
- **Proper Exception Handling**: Try-catch blocks
- **Meaningful Messages**: Error messages tiếng Việt
- **Logging**: Proper logging for debugging

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. 