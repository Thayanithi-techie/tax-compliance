package com.company.tax_compliance.service.rule;

import com.company.tax_compliance.domain.entity.TaxRuleEntity;
import com.company.tax_compliance.domain.entity.TransactionEntity;

public interface RuleExecutor {

    boolean supports(String ruleType);

    void execute(TransactionEntity transaction, TaxRuleEntity rule);
}
