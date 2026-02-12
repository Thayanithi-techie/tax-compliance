package com.company.tax_compliance.service.rule.impl;

import com.company.tax_compliance.domain.entity.TaxRuleEntity;
import com.company.tax_compliance.domain.entity.TransactionEntity;
import com.company.tax_compliance.domain.enums.Severity;
import com.company.tax_compliance.service.ExceptionService;
import com.company.tax_compliance.service.rule.RuleExecutor;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class HighValueTransactionRule implements RuleExecutor {

    private final ExceptionService exceptionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public HighValueTransactionRule(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
    }

    @Override
    public boolean supports(String ruleType) {
        return "HIGH_VALUE".equalsIgnoreCase(ruleType);
    }

    @Override
    public void execute(TransactionEntity transaction, TaxRuleEntity rule) {
        try {
            JsonNode config = objectMapper.readTree(rule.getRuleConfig());
            double threshold = config.get("thresholdAmount").asDouble();
            String severity = config.get("severity").asText();

            if (transaction.getAmount().doubleValue() > threshold) {
                exceptionService.createException(
                        transaction.getTransactionId(),
                        transaction.getCustomerId(),
                        rule.getRuleName(),
                        Severity.valueOf(severity),
                        "Transaction amount exceeds high value threshold"
                );
            }
        } catch (Exception ignored) {}
    }
}
