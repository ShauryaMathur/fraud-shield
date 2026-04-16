package com.fraudplatform.transaction_ingestion_service.service;

import com.fraudplatform.transaction_ingestion_service.dto.CreateTransactionRequest;
import com.fraudplatform.transaction_ingestion_service.dto.CreateTransactionResponse;
import com.fraudplatform.transaction_ingestion_service.enums.TransactionStatus;
import com.fraudplatform.transaction_ingestion_service.exception.TransactionNotFoundException;
import com.fraudplatform.transaction_ingestion_service.model.Transaction;
import com.fraudplatform.transaction_ingestion_service.producer.TransactionEventProducer;
import com.fraudplatform.transaction_ingestion_service.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    private static final String USER_ID = "user-1";

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionEventProducer transactionEventProducer;

    @Mock
    private UserActivityLogService userActivityLogService;

    @InjectMocks
    private TransactionService transactionService;

    private CreateTransactionRequest buildRequest() {
        return new CreateTransactionRequest("key-123", "merchant-1", 500.0, "USD", "US");
    }

    @Test
    void createTransaction_happyPath_SaveNPublish() {
        CreateTransactionRequest request = buildRequest();

        when(transactionRepository.findByIdempotencyKey(request.getIdempotencyKey()))
                .thenReturn(Optional.empty());

        Transaction saved = Transaction.builder()
                .id("txn-001")
                .userId(USER_ID)
                .merchantId(request.getMerchantId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .geo(request.getGeo())
                .status(TransactionStatus.CREATED)
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);

        CreateTransactionResponse response = transactionService.createTransaction(request, USER_ID);

        assertThat(response.getTransactionId()).isEqualTo("txn-001");
        assertThat(response.getStatus()).isEqualTo(TransactionStatus.CREATED);
        assertThat(response.getMessage()).isEqualTo("Transaction created successfully");

        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(transactionEventProducer, times(1)).publishTransactionCreated(any());
        verify(userActivityLogService, times(1))
                .log(eq(USER_ID), eq("TRANSACTION_CREATED"), anyMap());
    }

    @Test
    void createTransaction_duplicateIdempotencyKey_returnsExisting() {
        CreateTransactionRequest request = buildRequest();

        Transaction existing = Transaction.builder()
                .id("txn-existing")
                .status(TransactionStatus.CREATED)
                .build();

        when(transactionRepository.findByIdempotencyKey("key-123"))
                .thenReturn(Optional.of(existing));

        CreateTransactionResponse response = transactionService.createTransaction(request, USER_ID);

        assertThat(response.getTransactionId()).isEqualTo("txn-existing");
        assertThat(response.getMessage()).contains("Duplicate");

        verify(transactionRepository, never()).save(any());
        verify(transactionEventProducer, never()).publishTransactionCreated(any());
        verify(userActivityLogService, never()).log(anyString(), anyString(), anyMap());
    }

    @Test
    void getTransactionById_notFound_throwsException() {
        when(transactionRepository.findById("bad-id"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getTransactionById("bad-id"))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessageContaining("bad-id");
    }

    @Test
    void getTransactionById_found_returnsTransaction() {
        Transaction transaction = Transaction.builder()
                .id("txn-001")
                .userId(USER_ID)
                .build();

        when(transactionRepository.findById("txn-001"))
                .thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransactionById("txn-001");

        assertThat(result.getId()).isEqualTo("txn-001");
        assertThat(result.getUserId()).isEqualTo(USER_ID);
    }
}
