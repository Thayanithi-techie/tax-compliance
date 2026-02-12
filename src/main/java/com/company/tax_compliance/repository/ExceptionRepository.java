package com.company.tax_compliance.repository;

import com.company.tax_compliance.domain.entity.ExceptionEntity;
import com.company.tax_compliance.domain.enums.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExceptionRepository extends JpaRepository<ExceptionEntity, Long> {

    List<ExceptionEntity> findByCustomerId(String customerId);

    List<ExceptionEntity> findBySeverity(Severity severity);

    List<ExceptionEntity> findByRuleName(String ruleName);
    
    @Query("SELECT COUNT(e) FROM ExceptionEntity e")
    Long totalExceptions();

    @Query("SELECT e.severity, COUNT(e) FROM ExceptionEntity e GROUP BY e.severity")
    List<Object[]> countBySeverity();

    @Query("SELECT e.customerId, COUNT(e) FROM ExceptionEntity e GROUP BY e.customerId")
    List<Object[]> countByCustomer();

}
