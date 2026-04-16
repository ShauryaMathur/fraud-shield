package com.fraudplatform.fraud_rules_engine.fraud_rules;

import com.fraudplatform.fraud_rules_engine.model.TransactionEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;

@Component
public class OddHourRule implements FraudRule{
    @Override
    public boolean evaluate(TransactionEvent event) {

        Instant timestamp = event.getTimestamp();
        int hour = timestamp.atZone(ZoneOffset.UTC).getHour();

        return hour >= 1 && hour <= 5;
    }

    @Override
    public String getRuleName() {
        return "ODD_HOUR";
    }
}
