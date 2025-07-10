package com.example.syse.repository;

import com.example.syse.model.EmailTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {
    Optional<EmailTemplate> findByCode(String code);
    Optional<EmailTemplate> findByName(String name);
    List<EmailTemplate> findByStatus(Boolean status);
    List<EmailTemplate> findByCreatedBy_Id(Integer createdById);
    List<EmailTemplate> findByCodeContaining(String code);
    
    // Pagination methods
    Page<EmailTemplate> findByStatus(Boolean status, Pageable pageable);
    Page<EmailTemplate> findByCodeContaining(String code, Pageable pageable);
    Page<EmailTemplate> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCaseOrSubjectContainingIgnoreCase(
        String name, String code, String subject, Pageable pageable);
} 