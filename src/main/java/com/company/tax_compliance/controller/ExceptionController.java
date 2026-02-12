package com.company.tax_compliance.controller;

import com.company.tax_compliance.domain.entity.ExceptionEntity;
import com.company.tax_compliance.domain.enums.Severity;
import com.company.tax_compliance.repository.ExceptionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exceptions")
public class ExceptionController {

    private final ExceptionRepository exceptionRepository;

    public ExceptionController(ExceptionRepository exceptionRepository) {
        this.exceptionRepository = exceptionRepository;
    }

    @GetMapping
    public List<ExceptionEntity> getAllExceptions() {
        return exceptionRepository.findAll();
    }

    @GetMapping("/customer/{customerId}")
    public List<ExceptionEntity> getByCustomer(@PathVariable String customerId) {
        return exceptionRepository.findByCustomerId(customerId);
    }

    @GetMapping("/severity/{severity}")
    public List<ExceptionEntity> getBySeverity(@PathVariable Severity severity) {
        return exceptionRepository.findBySeverity(severity);
    }

    @GetMapping("/rule/{ruleName}")
    public List<ExceptionEntity> getByRule(@PathVariable String ruleName) {
        return exceptionRepository.findByRuleName(ruleName);
    }
}
