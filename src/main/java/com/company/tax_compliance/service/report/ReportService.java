package com.company.tax_compliance.service.report;

import com.company.tax_compliance.dto.CustomerTaxSummaryDTO;
import com.company.tax_compliance.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {

    private final TransactionRepository transactionRepository;

    public ReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<CustomerTaxSummaryDTO> getCustomerTaxSummary() {

        List<Object[]> rows = transactionRepository.getCustomerTaxSummaryRaw();
        List<CustomerTaxSummaryDTO> result = new ArrayList<>();

        for (Object[] row : rows) {
            String customerId = (String) row[0];
            BigDecimal totalAmount = (BigDecimal) row[1];
            BigDecimal totalReportedTax = (BigDecimal) row[2];
            BigDecimal totalExpectedTax = (BigDecimal) row[3];
            BigDecimal totalTaxGap = (BigDecimal) row[4];
            Long totalCount = (Long) row[5];
            Long nonCompliantCount = (Long) row[6];

            double complianceScore =
                    100 - ((double) nonCompliantCount / totalCount * 100);

            result.add(new CustomerTaxSummaryDTO(
                    customerId,
                    totalAmount,
                    totalReportedTax,
                    totalExpectedTax,
                    totalTaxGap,
                    complianceScore
            ));
        }
        return result;
    }
}
