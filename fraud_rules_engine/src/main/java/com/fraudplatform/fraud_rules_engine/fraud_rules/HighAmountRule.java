package com.fraudplatform.fraud_rules_engine.fraud_rules;

import com.fraudplatform.fraud_rules_engine.model.TransactionEvent;
import org.springframework.stereotype.Component;

@Component
public class HighAmountRule implements FraudRule{
    @Override
    public boolean evaluate(TransactionEvent event) {
        return event.getAmount() > 10000;
    }

    @Override
    public String getRuleName() {
        return "HIGH_AMOUNT";
    }
}
