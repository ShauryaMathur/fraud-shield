package com.fraudplatform.fraud_rules_engine.producer;

import com.fraudplatform.fraud_rules_engine.model.FraudEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FraudAlertProducer {

    private final KafkaTemplate<String, FraudEvent> kafkaTemplate;

    @Value("${kafka.topics.fraud-alert-created}")
    private String fraudAlertTopic;

    public void publishFraudEventAlert(FraudEvent fraudEvent){
        kafkaTemplate.send(fraudAlertTopic,fraudEvent.getUserId(),fraudEvent)
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error("Failed to publish Fraud Alert Event for txnId={} error={}", fraudEvent.getTransactionId(),error.getMessage());
                    }else{
                        log.info("Published Fraud Alert Event for txnId={} partition={} offset={}", fraudEvent.getTransactionId(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
                    }
                });
    }
}
