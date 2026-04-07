package com.fraudplatform.transaction_ingestion_service.model;

import com.fraudplatform.transaction_ingestion_service.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

// TransactionEvent.java — what goes on the Kafka topic
// keep this lean — it's a wire format, not a DB document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    private String transactionId;
    private String userId;
    private String merchantId;
    private Double amount;
    private String currency;
    private TransactionStatus status;
    private String geo;
    private Instant timestamp;
}
