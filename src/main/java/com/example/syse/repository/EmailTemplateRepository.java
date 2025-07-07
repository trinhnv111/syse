package com.example.syse.repository;

import com.example.syse.model.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    Optional<EmailTemplate> findByCode(String code);
    List<EmailTemplate> findByStatus(Boolean status);
    List<EmailTemplate> findByCreatedBy_Id(Integer createdById);
    List<EmailTemplate> findByCodeContaining(String code);
} 