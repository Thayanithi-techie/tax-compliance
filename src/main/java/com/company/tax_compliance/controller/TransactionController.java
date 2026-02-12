package com.company.tax_compliance.controller;

import com.company.tax_compliance.dto.TransactionRequestDTO;
import com.company.tax_compliance.dto.TransactionResponseDTO;
import com.company.tax_compliance.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/upload")
    public List<TransactionResponseDTO> uploadTransactions(
            @RequestBody @Valid List<TransactionRequestDTO> transactions) {
        return transactionService.processTransactions(transactions);
    }
}
