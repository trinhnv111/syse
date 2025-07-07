package com.example.syse.repository;

import com.example.syse.model.MessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
    List<MessageLog> findByUser_Id(Integer userId);
    List<MessageLog> findByChannel(String channel);
    List<MessageLog> findByStatus(String status);
    List<MessageLog> findBySentAtBetween(java.time.LocalDateTime from, java.time.LocalDateTime to);
} 