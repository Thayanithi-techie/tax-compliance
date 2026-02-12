package com.company.tax_compliance.service;

import com.company.tax_compliance.domain.entity.AuditLogEntity;
import com.company.tax_compliance.domain.entity.TransactionEntity;
import com.company.tax_compliance.domain.enums.ComplianceStatus;
import com.company.tax_compliance.domain.enums.EventType;
import com.company.tax_compliance.repository.AuditLogRepository;
import com.company.tax_compliance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
public class TaxCalculationService {

    private final TransactionRepository transactionRepository;
    private final AuditLogRepository auditLogRepository;

    public TaxCalculationService(TransactionRepository transactionRepository,
                                 AuditLogRepository auditLogRepository) {
        this.transactionRepository = transactionRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public void calculateTax(TransactionEntity transaction) {

        if (transaction.getReportedTax() == null) {
            transaction.setComplianceStatus(ComplianceStatus.NON_COMPLIANT);
            transactionRepository.save(transaction);
            logAudit(transaction.getTransactionId(), "Reported tax missing");
            return;
        }

        BigDecimal expectedTax = transaction.getAmount()
                .multiply(transaction.getTaxRate())
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal taxGap = expectedTax.subtract(transaction.getReportedTax())
                .setScale(2, RoundingMode.HALF_UP);

        transaction.setExpectedTax(expectedTax);
        transaction.setTaxGap(taxGap);

        transaction.setComplianceStatus(determineCompliance(taxGap));
        transactionRepository.save(transaction);

        logAudit(transaction.getTransactionId(),
                "ExpectedTax=" + expectedTax + ", TaxGap=" + taxGap);
    }

    private ComplianceStatus determineCompliance(BigDecimal taxGap) {

        if (taxGap.abs().compareTo(BigDecimal.ONE) <= 0) {
            return ComplianceStatus.COMPLIANT;
        }
        if (taxGap.compareTo(BigDecimal.ONE) > 0) {
            return ComplianceStatus.UNDERPAID;
        }
        return ComplianceStatus.OVERPAID;
    }

    private void logAudit(String transactionId, String detail) {
        AuditLogEntity audit = AuditLogEntity.builder()
                .eventType(EventType.TAX_COMPUTATION)
                .transactionId(transactionId)
                .detailJson("{\"detail\":\"" + detail + "\"}")
                .createdAt(LocalDateTime.now())
                .build();

        auditLogRepository.save(audit);
    }
}
