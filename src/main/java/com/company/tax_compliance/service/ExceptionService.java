package com.company.tax_compliance.service;

import com.company.tax_compliance.domain.entity.ExceptionEntity;
import com.company.tax_compliance.domain.enums.Severity;
import com.company.tax_compliance.repository.ExceptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExceptionService {

    private final ExceptionRepository exceptionRepository;

    public ExceptionService(ExceptionRepository exceptionRepository) {
        this.exceptionRepository = exceptionRepository;
    }

    public void createException(String transactionId,
                                String customerId,
                                String ruleName,
                                Severity severity,
                                String message) {

        ExceptionEntity exception = ExceptionEntity.builder()
                .transactionId(transactionId)
                .customerId(customerId)
                .ruleName(ruleName)
                .severity(severity)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();

        exceptionRepository.save(exception);
    }
}
