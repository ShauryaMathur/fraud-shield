package com.fraudplatform.transaction_ingestion_service.model;

import com.fraudplatform.transaction_ingestion_service.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

// Transaction.java — the MongoDB document
@Document(collection = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    private String id;

    // this is your idempotency key — client generates and sends this
    // unique index ensures duplicate requests don't create duplicate docs
    @Indexed(unique = true)
    private String idempotencyKey;

    private String userId;
    private String merchantId;
    private Double amount;
    private String currency;
    private TransactionStatus status;
    private String geo;
    private Instant createdAt;
    private List<String> riskFlags;
}
