package com.fraudplatform.transaction_ingestion_service.dto;

import com.fraudplatform.transaction_ingestion_service.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// CreateTransactionResponse.java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionResponse {
    private String transactionId;
    private TransactionStatus status;
    private String message;
}
