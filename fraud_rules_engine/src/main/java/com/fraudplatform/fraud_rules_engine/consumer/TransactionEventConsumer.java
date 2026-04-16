package com.fraudplatform.fraud_rules_engine.consumer;

import com.fraudplatform.fraud_rules_engine.fraud_rules.FraudRule;
import com.fraudplatform.fraud_rules_engine.model.FraudEvent;
import com.fraudplatform.fraud_rules_engine.model.TransactionEvent;
import com.fraudplatform.fraud_rules_engine.producer.FraudAlertProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionEventConsumer {

    private final FraudAlertProducer fraudAlertProducer;

    private final List<FraudRule> rules;

    private static FraudEvent buildAlertEvent(TransactionEvent transactionEvent, FraudRule rule, Instant timestamp) {
        return new FraudEvent(transactionEvent.getTransactionId(), transactionEvent.getUserId(), rule.getRuleName(),"LOW",timestamp);
    }

    @KafkaListener(id = "foo", topics = "${kafka.topics.transaction-created}")
    public void listen(TransactionEvent event) {
        log.info("Received transaction event {}", event);

        Instant now = Instant.now();

        rules.stream()
                .filter(rule -> rule.evaluate(event))
                .forEach(rule -> fraudAlertProducer.publishFraudEventAlert(buildAlertEvent(event,rule,now)));
    }
}
