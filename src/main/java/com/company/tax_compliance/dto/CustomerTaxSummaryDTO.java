package com.company.tax_compliance.dto;

import java.math.BigDecimal;

public class CustomerTaxSummaryDTO {

    private String customerId;
    private BigDecimal totalAmount;
    private BigDecimal totalReportedTax;
    private BigDecimal totalExpectedTax;
    private BigDecimal totalTaxGap;
    private Double complianceScore;

    public CustomerTaxSummaryDTO(String customerId,
                                 BigDecimal totalAmount,
                                 BigDecimal totalReportedTax,
                                 BigDecimal totalExpectedTax,
                                 BigDecimal totalTaxGap,
                                 Double complianceScore) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.totalReportedTax = totalReportedTax;
        this.totalExpectedTax = totalExpectedTax;
        this.totalTaxGap = totalTaxGap;
        this.complianceScore = complianceScore;
    }

}
