package com.company.tax_compliance.service;

import com.company.tax_compliance.domain.entity.TransactionEntity;
import com.company.tax_compliance.domain.enums.ComplianceStatus;
import com.company.tax_compliance.repository.AuditLogRepository;
import com.company.tax_compliance.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxCalculationServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AuditLogRepository auditLogRepository;

    @InjectMocks
    TaxCalculationService taxCalculationService;

    @Test
    void shouldMarkTransactionAsUnderpaid() {

        TransactionEntity txn = TransactionEntity.builder()
                .transactionId("TXN_TEST")
                .amount(new BigDecimal("10000"))
                .taxRate(new BigDecimal("0.18"))
                .reportedTax(new BigDecimal("1500"))
                .build();

        when(transactionRepository.save(txn)).thenReturn(txn);

        taxCalculationService.calculateTax(txn);

        assertEquals(ComplianceStatus.UNDERPAID, txn.getComplianceStatus());
        verify(transactionRepository).save(txn);
        verify(auditLogRepository).save(org.mockito.ArgumentMatchers.any());
    }
}
