package com.fraudplatform.transaction_ingestion_service.controller;

import com.fraudplatform.transaction_ingestion_service.dto.CreateTransactionRequest;
import com.fraudplatform.transaction_ingestion_service.dto.CreateTransactionResponse;
import com.fraudplatform.transaction_ingestion_service.model.Transaction;
import com.fraudplatform.transaction_ingestion_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTransactionResponse createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {

        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("Received transaction request userId={} amount={}",
                userId, request.getAmount());

        return transactionService.createTransaction(request, userId);
    }

    @GetMapping("/{transactionId}")
    public Transaction getTransaction(@PathVariable String transactionId) {
        // add this to repository: Optional<Transaction> findById(String id)
        return transactionService.getTransactionById(transactionId);
    }
}
