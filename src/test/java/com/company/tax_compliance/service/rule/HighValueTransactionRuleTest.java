package com.company.tax_compliance.service.rule;

import com.company.tax_compliance.domain.entity.TaxRuleEntity;
import com.company.tax_compliance.domain.entity.TransactionEntity;
import com.company.tax_compliance.service.ExceptionService;
import com.company.tax_compliance.service.rule.impl.HighValueTransactionRule;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class HighValueTransactionRuleTest {

    @Test
    void shouldCreateExceptionForHighValueTransaction() {

        ExceptionService exceptionService = mock(ExceptionService.class);
        HighValueTransactionRule rule = new HighValueTransactionRule(exceptionService);

        TransactionEntity txn = TransactionEntity.builder()
                .transactionId("TXN999")
                .customerId("CUST999")
                .amount(new BigDecimal("200000"))
                .build();

        TaxRuleEntity ruleEntity = TaxRuleEntity.builder()
                .ruleName("High Value Txn")
                .ruleType("HIGH_VALUE")
                .ruleConfig("{\"thresholdAmount\":100000,\"severity\":\"HIGH\"}")
                .build();

        rule.execute(txn, ruleEntity);

        verify(exceptionService).createException(
                eq("TXN999"),
                eq("CUST999"),
                eq("High Value Txn"),
                eq(com.company.tax_compliance.domain.enums.Severity.HIGH),
                anyString()
        );
    }
}
