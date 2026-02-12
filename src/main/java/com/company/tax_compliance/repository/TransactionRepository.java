package com.company.tax_compliance.repository;

import com.company.tax_compliance.domain.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByCustomerId(String customerId);

    List<TransactionEntity> findByValidationStatus(String validationStatus);
    
    @Query("""
    		SELECT t.customerId,
    		       SUM(t.amount),
    		       SUM(t.reportedTax),
    		       SUM(t.expectedTax),
    		       SUM(t.taxGap),
    		       COUNT(t),
    		       SUM(CASE WHEN t.complianceStatus = 'NON_COMPLIANT' THEN 1 ELSE 0 END)
    		FROM TransactionEntity t
    		GROUP BY t.customerId
    		""")
    		List<Object[]> getCustomerTaxSummaryRaw();
}