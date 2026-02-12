package com.company.tax_compliance.service;

import com.company.tax_compliance.domain.entity.TransactionEntity;
import com.company.tax_compliance.domain.enums.TransactionType;
import com.company.tax_compliance.dto.TransactionRequestDTO;
import com.company.tax_compliance.repository.AuditLogRepository;
import com.company.tax_compliance.repository.TransactionRepository;
import com.company.tax_compliance.service.rule.RuleEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    AuditLogRepository auditLogRepository;

    @Mock
    TaxCalculationService taxCalculationService;

    @Mock
    RuleEngine ruleEngine;

    @InjectMocks
    TransactionService transactionService;

    @Test
    void shouldProcessValidTransaction() {

        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setTransactionId("TXN_1");
        dto.setCustomerId("CUST_1");
        dto.setDate(LocalDate.now());
        dto.setAmount(new BigDecimal("5000"));
        dto.setTransactionType(TransactionType.SALE);

        transactionService.processTransactions(List.of(dto));

        verify(transactionRepository).save(org.mockito.ArgumentMatchers.any(TransactionEntity.class));
        verify(taxCalculationService).calculateTax(org.mockito.ArgumentMatchers.any());
        verify(ruleEngine).evaluate(org.mockito.ArgumentMatchers.any());
    }
}
