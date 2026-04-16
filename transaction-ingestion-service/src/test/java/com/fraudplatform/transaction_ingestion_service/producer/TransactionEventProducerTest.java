package com.fraudplatform.transaction_ingestion_service.producer;

import com.fraudplatform.transaction_ingestion_service.model.TransactionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionEventProducerTest {

    @Mock
    private KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    @InjectMocks
    private TransactionEventProducer eventProducer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(eventProducer, "transactionCreatedTopic", "transaction.created");
    }

    @Test
    void publishTransactionCreated_sendsToCorrectTopic() {
        TransactionEvent event = TransactionEvent.builder()
                .transactionId("txn-001")
                .userId("user-1")
                .build();

        // mock send() to return a completed future instead of null
        when(kafkaTemplate.send(any(String.class), any(String.class), any(TransactionEvent.class)))
                .thenReturn(new CompletableFuture<>());

        eventProducer.publishTransactionCreated(event);

        verify(kafkaTemplate, times(1))
                .send("transaction.created", "user-1", event);
    }
}
