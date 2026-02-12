package com.company.tax_compliance.audit;

import com.company.tax_compliance.domain.entity.AuditLogEntity;
import com.company.tax_compliance.domain.enums.EventType;
import com.company.tax_compliance.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(EventType eventType, String transactionId, String detailJson) {
        AuditLogEntity log = AuditLogEntity.builder()
                .eventType(eventType)
                .transactionId(transactionId)
                .detailJson(detailJson)
                .createdAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }
}
