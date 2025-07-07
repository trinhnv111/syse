USE syse;

CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,       -- Ví dụ: ADMIN, USER
    description VARCHAR(255)
);

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) UNIQUE,
    password VARCHAR(255),                  -- có thể NULL nếu dùng OAuth
    full_name VARCHAR(150),
    role_id INT,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
CREATE TABLE email_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,             -- Tên hiển thị
    code VARCHAR(100) UNIQUE NOT NULL,      -- RESET_PASS, ORDER_CONFIRM...
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,                  -- Nội dung HTML
    placeholders JSON,                      -- ["username", "resetLink"]
    status BOOLEAN DEFAULT TRUE,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
);
CREATE TABLE notification_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(100) UNIQUE NOT NULL,
    content TEXT NOT NULL,
    placeholders JSON,
    status BOOLEAN DEFAULT TRUE,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
);
CREATE TABLE message_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,                            -- Người nhận
    channel VARCHAR(20),                    -- "EMAIL" | "NOTIFICATION"
    template_code VARCHAR(100),
    content TEXT,                           -- Nội dung sau khi render
    status VARCHAR(50),                     -- SENT, FAILED...
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
