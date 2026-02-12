package com.company.tax_compliance.service;

import com.company.tax_compliance.domain.entity.AuditLogEntity;
import com.company.tax_compliance.domain.entity.TransactionEntity;
import com.company.tax_compliance.domain.enums.EventType;
import com.company.tax_compliance.domain.enums.ValidationStatus;
import com.company.tax_compliance.dto.TransactionRequestDTO;
import com.company.tax_compliance.dto.TransactionResponseDTO;
import com.company.tax_compliance.repository.AuditLogRepository;
import com.company.tax_compliance.repository.TransactionRepository;
import com.company.tax_compliance.service.rule.RuleEngine;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;
    private final TaxCalculationService taxCalculationService;
    private final RuleEngine ruleEngine;

    public TransactionService(TransactionRepository transactionRepository,
                              AuditLogRepository auditLogRepository,
                              TaxCalculationService taxCalculationService,
                              RuleEngine ruleEngine) {
        this.transactionRepository = transactionRepository;
        this.auditLogRepository = auditLogRepository;
        this.taxCalculationService = taxCalculationService;
        this.ruleEngine = ruleEngine;
    }

    public List<TransactionResponseDTO> processTransactions(List<TransactionRequestDTO> requestList) {

        List<TransactionResponseDTO> responses = new ArrayList<>();

        for (TransactionRequestDTO dto : requestList) {
            try {
                TransactionEntity entity = mapToEntity(dto);
                entity.setValidationStatus(ValidationStatus.SUCCESS);
                entity.setCreatedAt(LocalDateTime.now());

                transactionRepository.save(entity);
                logAudit(entity.getTransactionId(), EventType.INGESTION, "Transaction ingested");

                taxCalculationService.calculateTax(entity);
                ruleEngine.evaluate(entity);
                logAudit(entity.getTransactionId(), EventType.RULE_EXECUTION, "Rules evaluated");
                responses.add(new TransactionResponseDTO(
                        dto.getTransactionId(), "SUCCESS", "Transaction processed successfully"));

            } catch (Exception ex) {
                responses.add(new TransactionResponseDTO(
                        dto.getTransactionId(), "FAILURE", ex.getMessage()));
            }
        }
        return responses;
    }

    private TransactionEntity mapToEntity(TransactionRequestDTO dto) {
        return TransactionEntity.builder()
                .transactionId(dto.getTransactionId())
                .transactionDate(dto.getDate())
                .customerId(dto.getCustomerId())
                .amount(dto.getAmount())
                .taxRate(dto.getTaxRate())
                .reportedTax(dto.getReportedTax())
                .transactionType(dto.getTransactionType())
                .build();
    }

    private void logAudit(String transactionId, EventType eventType, String detail) {
        AuditLogEntity log = AuditLogEntity.builder()
                .transactionId(transactionId)
                .eventType(eventType)
                .detailJson("{\"message\":\"" + detail + "\"}")
                .createdAt(LocalDateTime.now())
                .build();
        auditLogRepository.save(log);
    }
}
