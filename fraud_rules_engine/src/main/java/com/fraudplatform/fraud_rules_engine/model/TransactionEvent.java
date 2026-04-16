package com.fraudplatform.fraud_rules_engine.model;

import com.fraudplatform.fraud_rules_engine.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
