package com.fraudplatform.transaction_ingestion_service.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraudplatform.transaction_ingestion_service.model.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionEventProducer {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    @Value("${kafka.topics.transaction-created}")
    private String transactionCreatedTopic;

    public void publishTransactionCreated(TransactionEvent event) {

        // key = userId — guarantees ordering per user across partitions
        kafkaTemplate.send(transactionCreatedTopic, event.getUserId(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish transaction event txnId={} error={}",
                                event.getTransactionId(), ex.getMessage());
                    } else {
                        log.info("Published transaction event txnId={} partition={} offset={}",
                                event.getTransactionId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });

    }
}
