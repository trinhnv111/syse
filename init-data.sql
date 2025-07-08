-- Script khởi tạo dữ liệu mẫu cho SYSE
USE syse;

-- Xóa dữ liệu cũ (nếu có)
DELETE FROM message_logs;
DELETE FROM email_templates;
DELETE FROM notification_templates;
DELETE FROM users;
DELETE FROM roles;

-- Tạo roles
INSERT INTO roles (name, description) VALUES 
('ADMIN', 'Administrator - Full access to all features'),
('USER', 'Regular user - Limited access');

-- Tạo admin user (password: admin123)
INSERT INTO users (username, email, password, full_name, role_id, enabled) VALUES 
('admin', 'admin@syse.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'System Administrator', 1, true);

-- Tạo regular user (password: user123)
INSERT INTO users (username, email, password, full_name, role_id, enabled) VALUES 
('user', 'user@syse.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Regular User', 2, true);

-- Tạo email templates mẫu
INSERT INTO email_templates (name, code, subject, content, placeholders, status, created_by) VALUES 
('Welcome Email', 'WELCOME', 'Chào mừng bạn đến với SYSE', '<h1>Chào mừng {{username}}!</h1><p>Cảm ơn bạn đã đăng ký.</p>', '["username"]', true, 1),
('Password Reset', 'RESET_PASSWORD', 'Đặt lại mật khẩu', '<h1>Đặt lại mật khẩu</h1><p>Click vào link: {{resetLink}}</p>', '["resetLink"]', true, 1);

-- Tạo notification templates mẫu
INSERT INTO notification_templates (name, code, content, placeholders, status, created_by) VALUES 
('Welcome Notification', 'WELCOME_NOTIF', 'Chào mừng {{username}} đến với hệ thống!', '["username"]', true, 1),
('System Alert', 'SYSTEM_ALERT', 'Cảnh báo hệ thống: {{message}}', '["message"]', true, 1);

SELECT 'Data initialization completed!' as status; 