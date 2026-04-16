package com.fraudplatform.transaction_ingestion_service.service;

import com.fraudplatform.transaction_ingestion_service.dto.CreateTransactionRequest;
import com.fraudplatform.transaction_ingestion_service.dto.CreateTransactionResponse;
import com.fraudplatform.transaction_ingestion_service.enums.TransactionStatus;
import com.fraudplatform.transaction_ingestion_service.exception.TransactionNotFoundException;
import com.fraudplatform.transaction_ingestion_service.model.Transaction;
import com.fraudplatform.transaction_ingestion_service.model.TransactionEvent;
import com.fraudplatform.transaction_ingestion_service.producer.TransactionEventProducer;
import com.fraudplatform.transaction_ingestion_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionEventProducer eventProducer;
    private final UserActivityLogService userActivityLogService;

    public Transaction getTransactionById(String transactionId){
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isPresent()){
            return transaction.get();
        }else{
            throw new TransactionNotFoundException("Transaction with ID: " + transactionId + " not found");
        }
    }

    public CreateTransactionResponse createTransaction(CreateTransactionRequest request, String userId) {

        // idempotency check — same key seen before? return existing result
        Optional<Transaction> existing =
                transactionRepository.findByIdempotencyKey(request.getIdempotencyKey());

        if (existing.isPresent()) {
            log.info("Duplicate request detected idempotencyKey={}", request.getIdempotencyKey());
            return CreateTransactionResponse.builder()
                    .transactionId(existing.get().getId())
                    .status(existing.get().getStatus())
                    .message("Duplicate request — returning existing transaction")
                    .build();
        }
        Instant createdAt = Instant.now();

        // build and persist the transaction
        Transaction transaction = Transaction.builder()
                .idempotencyKey(request.getIdempotencyKey())
                .userId(userId)
                .merchantId(request.getMerchantId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .geo(request.getGeo())
                .status(TransactionStatus.CREATED)
                .createdAt(createdAt)
                .riskFlags(new ArrayList<>())
                .build();

        Transaction saved = transactionRepository.save(transaction);

        Map<String, Object> metadata = Map.of(
                "transactionId", saved.getId(),
                "amount", saved.getAmount(),
                "geo", saved.getGeo(),
                "merchantId", saved.getMerchantId()
        );

        userActivityLogService.log(userId, "TRANSACTION_CREATED", metadata);

        // publish to Kafka — downstream services react from here
        TransactionEvent event = TransactionEvent.builder()
                .transactionId(saved.getId())
                .userId(saved.getUserId())
                .merchantId(saved.getMerchantId())
                .amount(saved.getAmount())
                .currency(saved.getCurrency())
                .geo(saved.getGeo())
                .status(saved.getStatus())
                .timestamp(createdAt)
                .build();

        eventProducer.publishTransactionCreated(event);

        return CreateTransactionResponse.builder()
                .transactionId(saved.getId())
                .status(saved.getStatus())
                .message("Transaction created successfully")
                .build();
    }
}
