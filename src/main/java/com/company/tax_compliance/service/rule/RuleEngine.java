package com.company.tax_compliance.service.rule;

import com.company.tax_compliance.domain.entity.TaxRuleEntity;
import com.company.tax_compliance.domain.entity.TransactionEntity;
import com.company.tax_compliance.repository.TaxRuleRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RuleEngine {

    private final TaxRuleRepository taxRuleRepository;
    private final List<RuleExecutor> executors;

    public RuleEngine(TaxRuleRepository taxRuleRepository,
                      List<RuleExecutor> executors) {
        this.taxRuleRepository = taxRuleRepository;
        this.executors = executors;
    }

    public void evaluate(TransactionEntity transaction) {

        List<TaxRuleEntity> rules = taxRuleRepository.findByEnabledTrue();

        for (TaxRuleEntity rule : rules) {
            executors.stream()
                    .filter(executor -> executor.supports(rule.getRuleType()))
                    .findFirst()
                    .ifPresent(executor ->
                            executor.execute(transaction, rule)
                    );
        }
    }
}
