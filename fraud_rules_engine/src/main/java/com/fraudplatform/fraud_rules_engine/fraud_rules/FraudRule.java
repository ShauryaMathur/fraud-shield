package com.fraudplatform.fraud_rules_engine.fraud_rules;

import com.fraudplatform.fraud_rules_engine.model.FraudEvent;
import com.fraudplatform.fraud_rules_engine.model.TransactionEvent;

public interface FraudRule {
    public boolean evaluate(TransactionEvent event);

    public String getRuleName();
}
