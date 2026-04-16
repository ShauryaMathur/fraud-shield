package com.fraudplatform.transaction_ingestion_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraudplatform.transaction_ingestion_service.dto.CreateTransactionRequest;
import com.fraudplatform.transaction_ingestion_service.dto.CreateTransactionResponse;
import com.fraudplatform.transaction_ingestion_service.enums.TransactionStatus;
import com.fraudplatform.transaction_ingestion_service.exception.TransactionNotFoundException;
import com.fraudplatform.transaction_ingestion_service.model.Transaction;
import com.fraudplatform.transaction_ingestion_service.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Loads only web layer — no MongoDB, no Kafka
// TransactionService is mocked via @MockBean
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // @MockBean registers mock in Spring context — different from @Mock
    // @Mock is pure Mockito, @MockBean is Spring-aware
    @MockBean
    private TransactionService transactionService;

    private CreateTransactionRequest buildValidRequest() {
        return new CreateTransactionRequest(
                "key-123", "merchant-1", 500.0, "USD", "US"
        );
    }

    @Test
    void createTransaction_validRequest_returns201() throws Exception {
        CreateTransactionResponse response = CreateTransactionResponse.builder()
                .transactionId("txn-001")
                .status(TransactionStatus.CREATED)
                .message("Transaction created successfully")
                .build();

        when(transactionService.createTransaction(any(),"user-1")).thenReturn(response);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildValidRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("txn-001"))
                .andExpect(jsonPath("$.message").value("Transaction created successfully"));
    }

    @Test
    void createTransaction_missingUserId_returns400() throws Exception {
        // userId is blank — violates @NotBlank
        CreateTransactionRequest invalid = new CreateTransactionRequest(
                "key-123", "merchant-1", 500.0, "USD", "US"
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTransaction_negativeAmount_returns400() throws Exception {
        // amount is negative — violates @Positive
        CreateTransactionRequest invalid = new CreateTransactionRequest(
                "key-123", "merchant-1", -100.0, "USD", "US"
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTransaction_nullAmount_returns400() throws Exception {
        // amount is null — violates @NotNull
        CreateTransactionRequest invalid = new CreateTransactionRequest(
                "key-123", "merchant-1", null, "USD", "US"
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTransaction_found_returns200() throws Exception {
        Transaction transaction = Transaction.builder()
                .id("txn-001")
                .userId("user-1")
                .build();

        when(transactionService.getTransactionById("txn-001")).thenReturn(transaction);

        mockMvc.perform(get("/api/transactions/txn-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("txn-001"))
                .andExpect(jsonPath("$.userId").value("user-1"));
    }

    @Test
    void getTransaction_notFound_returns500() throws Exception {
        // your service throws generic Exception — Spring maps this to 500
        // ideally you'd throw a custom NotFoundException mapped to 404
        when(transactionService.getTransactionById("bad-id"))
                .thenThrow(new TransactionNotFoundException("Transaction with ID: bad-id not found"));

        mockMvc.perform(get("/api/transactions/bad-id"))
                .andExpect(status().isNotFound());
    }
}
