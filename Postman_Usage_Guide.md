# Hướng dẫn sử dụng Postman với Email Template API

## Vấn đề bạn gặp phải

**Request sai format:**
```json
{
  "placeholders": "[\"customerName\", \"orderNumber\", \"totalAmount\", \"orderDate\"]"
}
```

**Request đúng format:**
```json
{
  "placeholders": {
    "customerName": "",
    "orderNumber": "",
    "totalAmount": "",
    "orderDate": ""
  }
}
```

## Các Request JSON đúng format

### 1. Tạo Email Template mới
```json
POST http://localhost:8080/api/email-templates
Content-Type: application/json
Authorization: Bearer {{jwt_token}}

{
  "name": "Order Confirmation",
  "code": "ORDER_CONFIRM",
  "subject": "Xác nhận đơn hàng {{orderNumber}}",
  "content": "<h2>Xác nhận đơn hàng</h2><p>Xin chào {{customerName}},</p><p>Cảm ơn bạn đã đặt hàng. Chi tiết đơn hàng:</p><ul><li>Mã đơn hàng: {{orderNumber}}</li><li>Tổng tiền: {{totalAmount}}</li><li>Ngày đặt: {{orderDate}}</li></ul><p>Chúng tôi sẽ xử lý đơn hàng của bạn sớm nhất có thể.</p>",
  "placeholders": {
    "customerName": "",
    "orderNumber": "",
    "totalAmount": "",
    "orderDate": ""
  },
  "status": true
}
```

### 2. Cập nhật Email Template (Sửa lỗi của bạn)
```json
PUT http://localhost:8080/api/email-templates/12
Content-Type: application/json
Authorization: Bearer {{jwt_token}}

{
  "name": "Order Confirmation UPDATE",
  "code": "ORDER_CONFIRM_UPDATE",
  "subject": "Xác nhận đơn hàng UPDATE",
  "content": "<h2>Xác nhận đơn hàng</h2><p>Xin chào {{customerName}},</p><p>Cảm ơn bạn đã đặt hàng. Chi tiết đơn hàng:</p><ul><li>Mã đơn hàng: {{orderNumber}}</li><li>Tổng tiền: {{totalAmount}}</li><li>Ngày đặt: {{orderDate}}</li></ul><p>Chúng tôi sẽ xử lý đơn hàng của bạn sớm nhất có thể.</p>",
  "placeholders": {
    "customerName": "",
    "orderNumber": "",
    "totalAmount": "",
    "orderDate": ""
  },
  "status": true
}
```

### 3. Lấy chi tiết template với render
```json
GET http://localhost:8080/api/email-templates/12/detail-with-render?placeholders[customerName]=John Doe&placeholders[orderNumber]=ORD-001&placeholders[totalAmount]=1,500,000 VND&placeholders[orderDate]=2024-01-15
Authorization: Bearer {{jwt_token}}
```

### 4. Render template
```json
POST http://localhost:8080/api/email-templates/render
Content-Type: application/json
Authorization: Bearer {{jwt_token}}

{
  "content": "<h2>Xác nhận đơn hàng</h2><p>Xin chào {{customerName}},</p><p>Cảm ơn bạn đã đặt hàng. Chi tiết đơn hàng:</p><ul><li>Mã đơn hàng: {{orderNumber}}</li><li>Tổng tiền: {{totalAmount}}</li><li>Ngày đặt: {{orderDate}}</li></ul>",
  "placeholders": {
    "customerName": "John Doe",
    "orderNumber": "ORD-001",
    "totalAmount": "1,500,000 VND",
    "orderDate": "2024-01-15"
  }
}
```

## Các lỗi thường gặp và cách sửa

### 1. Lỗi "Đã xảy ra lỗi hệ thống"
**Nguyên nhân:** 
- Placeholders sai format (array thay vì object)
- Code template có ký tự đặc biệt
- Validation thất bại

**Cách sửa:**
```json
// SAI
"placeholders": "[\"customerName\", \"orderNumber\"]"

// ĐÚNG
"placeholders": {
  "customerName": "",
  "orderNumber": ""
}
```

### 2. Lỗi "Mã template chỉ được chứa chữ cái, số, dấu gạch ngang và dấu gạch dưới"
**Nguyên nhân:** Code có khoảng trắng hoặc ký tự đặc biệt

**Cách sửa:**
```json
// SAI
"code": "ORDER_CONFIRM UPDATE"

// ĐÚNG
"code": "ORDER_CONFIRM_UPDATE"
```

### 3. Lỗi "Tên template đã tồn tại"
**Nguyên nhân:** Template với tên này đã tồn tại

**Cách sửa:** Đổi tên template hoặc sử dụng template khác

## Cách import vào Postman

1. Tạo Collection mới trong Postman
2. Import file `Postman_Test_JSON.json`
3. Thay đổi `{{jwt_token}}` bằng token thực của bạn
4. Chạy từng request để test

## Test Cases

### Test Case 1: Tạo template thành công
1. Sử dụng request "1. Tạo Email Template mới"
2. Kiểm tra response có `success: true`
3. Lưu ID template để test tiếp

### Test Case 2: Cập nhật template
1. Sử dụng request "2. Cập nhật Email Template"
2. Thay đổi ID trong URL thành ID thực
3. Kiểm tra response thành công

### Test Case 3: Render template
1. Sử dụng request "4. Lấy chi tiết template với render"
2. Kiểm tra `renderedContent` đã được render đúng
3. Kiểm tra placeholders được thay thế

## Lưu ý quan trọng

1. **Placeholders phải là object, không phải array**
2. **Code template không được có khoảng trắng**
3. **Cần có JWT token hợp lệ**
4. **Content và subject phải có độ dài tối thiểu**
5. **Tên và code template phải unique** 