package com.company.tax_compliance.service.rule.impl;

import org.springframework.stereotype.Component;
import com.company.tax_compliance.domain.entity.TaxRuleEntity;
import com.company.tax_compliance.domain.entity.TransactionEntity;
import com.company.tax_compliance.domain.enums.Severity;
import com.company.tax_compliance.service.ExceptionService;
import com.company.tax_compliance.service.rule.RuleExecutor;

@Component
public class RefundValidationRule implements RuleExecutor {

    private final ExceptionService exceptionService;

    public RefundValidationRule(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
    }

    @Override
    public boolean supports(String ruleType) {
        return "REFUND_VALIDATION".equalsIgnoreCase(ruleType);
    }

    @Override
    public void execute(TransactionEntity transaction, TaxRuleEntity rule) {
        if ("REFUND".equalsIgnoreCase(transaction.getTransactionType().name())) {
            if (transaction.getAmount().doubleValue() > 10000) { 
                exceptionService.createException(
                        transaction.getTransactionId(),
                        transaction.getCustomerId(),
                        rule.getRuleName(),
                        Severity.HIGH,
                        "Refund amount exceeds original sale"
                );
            }
        }
    }
}
