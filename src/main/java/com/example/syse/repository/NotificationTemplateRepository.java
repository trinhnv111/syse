package com.example.syse.repository;

import com.example.syse.model.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByCode(String code);
    List<NotificationTemplate> findByStatus(Boolean status);
    List<NotificationTemplate> findByCreatedBy_Id(Integer createdById);
    List<NotificationTemplate> findByCodeContaining(String code);
} 