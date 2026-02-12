package com.company.tax_compliance.controller;

import com.company.tax_compliance.dto.CustomerTaxSummaryDTO;
import com.company.tax_compliance.repository.ExceptionRepository;
import com.company.tax_compliance.service.report.ReportService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final ExceptionRepository exceptionRepository;

    public ReportController(ReportService reportService,
    		ExceptionRepository exceptionRepository) {
        this.reportService = reportService;
        this.exceptionRepository = exceptionRepository;
    }

    @GetMapping("/customer-tax-summary")
    public List<CustomerTaxSummaryDTO> customerTaxSummary() {
        return reportService.getCustomerTaxSummary();
    }
    
    @GetMapping("/exception-summary")
    public Object exceptionSummary() {

        return Map.of(
                "totalExceptions", exceptionRepository.totalExceptions(),
                "bySeverity", exceptionRepository.countBySeverity(),
                "byCustomer", exceptionRepository.countByCustomer()
        );
    }

}
