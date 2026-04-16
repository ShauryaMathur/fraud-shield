package com.fraudplatform.fraud_rules_engine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FraudEvent {
    private String transactionId;
    private String userId;
    private String ruleTriggered;
    private String severity;
    private Instant timestamp;
}
