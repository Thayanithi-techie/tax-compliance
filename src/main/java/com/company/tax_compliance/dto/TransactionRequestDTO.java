package com.company.tax_compliance.dto;

import com.company.tax_compliance.domain.enums.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequestDTO {

    @NotBlank
    private String transactionId;

    @NotNull
    private LocalDate date;

    @NotBlank
    private String customerId;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    private BigDecimal taxRate;
    private BigDecimal reportedTax;

    @NotNull
    private TransactionType transactionType;

}
