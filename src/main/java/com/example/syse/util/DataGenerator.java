package com.example.syse.util;

import com.example.syse.model.EmailTemplate;
import com.example.syse.model.NotificationTemplate;
import com.example.syse.model.User;
import com.example.syse.model.Role;
import com.example.syse.model.MessageLog;
import com.example.syse.repository.EmailTemplateRepository;
import com.example.syse.repository.NotificationTemplateRepository;
import com.example.syse.repository.UserRepository;
import com.example.syse.repository.RoleRepository;
import com.example.syse.repository.MessageLogRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class DataGenerator implements CommandLineRunner {

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MessageLogRepository messageLogRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Faker faker = new Faker(new Locale("vi"));

    /**
     * Utility method to create JSON array string from string array
     */
    private String createJsonArray(String... items) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < items.length; i++) {
            json.append("\"").append(items[i]).append("\"");
            if (i < items.length - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    @Override
    public void run(String... args) throws Exception {
        // Chỉ chạy khi cần generate dữ liệu
        // generateData();
    }

    public void generateData() {
        System.out.println("Bắt đầu generate dữ liệu...");
        
        // Generate roles trước
        generateRoles();
        
        // Generate users
        generateUsers(100);
        
        // Generate email templates
        generateEmailTemplates(50);
        
        // Generate notification templates
        generateNotificationTemplates(30);
        
        // Generate message logs
        generateMessageLogs(200);
        
        System.out.println("Hoàn thành generate dữ liệu!");
    }

    private void generateRoles() {
        if (roleRepository.count() == 0) {
            List<Role> roles = Arrays.asList(
                createRole("ADMIN", "Quản trị viên"),
                createRole("USER", "Người dùng"),
                createRole("MODERATOR", "Điều hành viên"),
                createRole("SUPPORT", "Hỗ trợ viên")
            );
            roleRepository.saveAll(roles);
            System.out.println("Đã tạo " + roles.size() + " roles");
        }
    }

    private Role createRole(String name, String description) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        return role;
    }

    public void generateUsers(int count) {
        List<User> users = new ArrayList<>();
        List<Role> roles = roleRepository.findAll();

        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setUsername(faker.name().username());
            user.setEmail(faker.internet().emailAddress());
            user.setPassword(passwordEncoder.encode("password123"));
            user.setFullName(faker.name().fullName());
            user.setEnabled(true);
            user.setRole(roles.get(ThreadLocalRandom.current().nextInt(roles.size())));
            users.add(user);
        }

        userRepository.saveAll(users);
        System.out.println("Đã tạo " + users.size() + " users");
    }

    public void generateEmailTemplates(int count) {
        List<EmailTemplate> templates = new ArrayList<>();
        List<User> users = userRepository.findAll();

        String[] templateTypes = {
            "WELCOME", "PASSWORD_RESET", "ACCOUNT_VERIFICATION", "NEWSLETTER", 
            "PROMOTION", "ORDER_CONFIRMATION", "SHIPPING_NOTIFICATION", "PAYMENT_RECEIVED",
            "ACCOUNT_UPDATE", "SECURITY_ALERT", "BIRTHDAY_WISH", "ANNOUNCEMENT"
        };

        String[] subjects = {
            "Chào mừng bạn đến với SYSE",
            "Đặt lại mật khẩu",
            "Xác thực tài khoản",
            "Bản tin hàng tuần",
            "Ưu đãi đặc biệt",
            "Xác nhận đơn hàng",
            "Thông báo vận chuyển",
            "Xác nhận thanh toán",
            "Cập nhật tài khoản",
            "Cảnh báo bảo mật",
            "Chúc mừng sinh nhật",
            "Thông báo quan trọng"
        };

        for (int i = 0; i < count; i++) {
            EmailTemplate template = new EmailTemplate();
            template.setName(faker.lorem().sentence(3, 5));
            template.setCode(templateTypes[i % templateTypes.length] + "_" + (i + 1));
            template.setSubject(subjects[i % subjects.length]);
            template.setContent(generateEmailContent(i));
            template.setPlaceholders(generatePlaceholders(i));
            template.setStatus(faker.random().nextBoolean());
            template.setCreatedBy(users.get(ThreadLocalRandom.current().nextInt(users.size())));
            templates.add(template);
        }

        emailTemplateRepository.saveAll(templates);
        System.out.println("Đã tạo " + templates.size() + " email templates");
    }

    private String generateEmailContent(int index) {
        String[] contents = {
            "<h1>Xin chào {{username}}!</h1><p>Cảm ơn bạn đã đăng ký tài khoản tại SYSE.</p><p>Chúc bạn có trải nghiệm tốt!</p>",
            "<h2>Đặt lại mật khẩu</h2><p>Xin chào {{fullName}},</p><p>Bạn đã yêu cầu đặt lại mật khẩu. Vui lòng click vào link bên dưới:</p><a href='{{resetLink}}'>Đặt lại mật khẩu</a>",
            "<h2>Xác thực tài khoản</h2><p>Xin chào {{fullName}},</p><p>Vui lòng xác thực email của bạn bằng cách click vào link: <a href='{{verificationLink}}'>Xác thực</a></p>",
            "<h2>Bản tin tuần</h2><p>Xin chào {{username}},</p><p>Đây là bản tin hàng tuần của chúng tôi với những tin tức mới nhất.</p>",
            "<h2>Ưu đãi đặc biệt</h2><p>Xin chào {{fullName}},</p><p>Chúng tôi có một ưu đãi đặc biệt dành cho bạn: {{discountCode}}</p>",
            "<h2>Xác nhận đơn hàng</h2><p>Xin chào {{fullName}},</p><p>Cảm ơn bạn đã đặt hàng. Mã đơn hàng: {{orderNumber}}</p>",
            "<h2>Thông báo vận chuyển</h2><p>Xin chào {{fullName}},</p><p>Đơn hàng {{orderNumber}} của bạn đã được vận chuyển.</p>",
            "<h2>Xác nhận thanh toán</h2><p>Xin chào {{fullName}},</p><p>Chúng tôi đã nhận được thanh toán cho đơn hàng {{orderNumber}}.</p>",
            "<h2>Cập nhật tài khoản</h2><p>Xin chào {{username}},</p><p>Thông tin tài khoản của bạn đã được cập nhật thành công.</p>",
            "<h2>Cảnh báo bảo mật</h2><p>Xin chào {{fullName}},</p><p>Chúng tôi phát hiện hoạt động đăng nhập bất thường từ {{location}}.</p>",
            "<h2>Chúc mừng sinh nhật!</h2><p>Xin chào {{fullName}},</p><p>Chúc mừng sinh nhật! Chúng tôi có một món quà đặc biệt dành cho bạn.</p>",
            "<h2>Thông báo quan trọng</h2><p>Xin chào {{fullName}},</p><p>{{announcement}}</p>"
        };
        return contents[index % contents.length];
    }

    private String generatePlaceholders(int index) {
        String[][] placeholders = {
            {"username", "fullName"},
            {"fullName", "resetLink"},
            {"fullName", "verificationLink"},
            {"username"},
            {"fullName", "discountCode"},
            {"fullName", "orderNumber"},
            {"fullName", "orderNumber"},
            {"fullName", "orderNumber"},
            {"username"},
            {"fullName", "location"},
            {"fullName"},
            {"fullName", "announcement"}
        };
        
        return createJsonArray(placeholders[index % placeholders.length]);
    }

    public void generateNotificationTemplates(int count) {
        List<NotificationTemplate> templates = new ArrayList<>();
        List<User> users = userRepository.findAll();

        String[] notificationTypes = {
            "PUSH_NOTIFICATION", "SMS", "IN_APP", "WEBHOOK", "SLACK", "TELEGRAM"
        };

        for (int i = 0; i < count; i++) {
            NotificationTemplate template = new NotificationTemplate();
            template.setName(faker.lorem().sentence(3, 5));
            template.setCode(notificationTypes[i % notificationTypes.length] + "_" + (i + 1));
            template.setContent(faker.lorem().paragraph());
            template.setPlaceholders(createJsonArray("username", "fullName"));
            template.setStatus(faker.random().nextBoolean());
            template.setCreatedBy(users.get(ThreadLocalRandom.current().nextInt(users.size())));
            templates.add(template);
        }

        notificationTemplateRepository.saveAll(templates);
        System.out.println("Đã tạo " + templates.size() + " notification templates");
    }

    public void generateMessageLogs(int count) {
        List<MessageLog> logs = new ArrayList<>();
        List<User> users = userRepository.findAll();
        List<EmailTemplate> emailTemplates = emailTemplateRepository.findAll();

        String[] channels = {"EMAIL", "NOTIFICATION"};
        String[] statuses = {"SENT", "FAILED", "PENDING", "DELIVERED"};

        for (int i = 0; i < count; i++) {
            MessageLog log = new MessageLog();
            log.setUser(users.get(ThreadLocalRandom.current().nextInt(users.size())));
            log.setChannel(channels[ThreadLocalRandom.current().nextInt(channels.length)]);
            log.setContent(faker.lorem().paragraph());
            log.setStatus(statuses[ThreadLocalRandom.current().nextInt(statuses.length)]);
            
            if (!emailTemplates.isEmpty()) {
                log.setTemplateCode(emailTemplates.get(ThreadLocalRandom.current().nextInt(emailTemplates.size())).getCode());
            }
            
            logs.add(log);
        }

        messageLogRepository.saveAll(logs);
        System.out.println("Đã tạo " + logs.size() + " message logs");
    }

    // Getter methods for repositories
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public EmailTemplateRepository getEmailTemplateRepository() {
        return emailTemplateRepository;
    }

    public NotificationTemplateRepository getNotificationTemplateRepository() {
        return notificationTemplateRepository;
    }

    public MessageLogRepository getMessageLogRepository() {
        return messageLogRepository;
    }

    public RoleRepository getRoleRepository() {
        return roleRepository;
    }
} 