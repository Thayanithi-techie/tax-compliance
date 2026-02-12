package com.company.tax_compliance.repository;

import com.company.tax_compliance.domain.entity.TaxRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaxRuleRepository extends JpaRepository<TaxRuleEntity, Long> {

    List<TaxRuleEntity> findByEnabledTrue();
}

