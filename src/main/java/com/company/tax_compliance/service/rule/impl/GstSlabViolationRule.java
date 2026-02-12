package com.company.tax_compliance.service.rule.impl;

import org.springframework.stereotype.Component;

import com.company.tax_compliance.domain.entity.TaxRuleEntity;
import com.company.tax_compliance.domain.entity.TransactionEntity;
import com.company.tax_compliance.domain.enums.Severity;
import com.company.tax_compliance.service.ExceptionService;
import com.company.tax_compliance.service.rule.RuleExecutor;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class GstSlabViolationRule implements RuleExecutor {

    private final ExceptionService exceptionService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GstSlabViolationRule(ExceptionService exceptionService) {
        this.exceptionService = exceptionService;
    }

    @Override
    public boolean supports(String ruleType) {
        return "GST_SLAB".equalsIgnoreCase(ruleType);
    }

    @Override
    public void execute(TransactionEntity transaction, TaxRuleEntity rule) {
        try {
            JsonNode config = objectMapper.readTree(rule.getRuleConfig());
            double slabAmount = config.get("slabAmount").asDouble();
            double minTaxRate = config.get("minTaxRate").asDouble();

            if (transaction.getAmount().doubleValue() > slabAmount &&
                    transaction.getTaxRate().doubleValue() < minTaxRate) {

                exceptionService.createException(
                        transaction.getTransactionId(),
                        transaction.getCustomerId(),
                        rule.getRuleName(),
                        Severity.MEDIUM,
                        "GST slab violation detected"
                );
            }
        } catch (Exception ignored) {}
    }
}
