package com.company.tax_compliance.dto;

public class TransactionResponseDTO {

    private String transactionId;
    private String status;
    private String message;

    public TransactionResponseDTO(String transactionId, String status, String message) {
        this.transactionId = transactionId;
        this.status = status;
        this.message = message;
    }

}