package com.fraudplatform.fraud_rules_engine.fraud_rules;

import com.fraudplatform.fraud_rules_engine.model.TransactionEvent;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class GeoRule implements FraudRule{

    private static final Set<String> HIGH_RISK_COUNTRIES = Set.of(
            "KP",  // North Korea
            "IR",  // Iran
            "CU",  // Cuba
            "SY",  // Syria
            "RU"   // Russia
    );

    @Override
    public boolean evaluate(TransactionEvent event) {
        if (event.getGeo() == null || event.getGeo().isBlank()) {
            return false;
        }
        return HIGH_RISK_COUNTRIES.contains(event.getGeo().toUpperCase());
    }

    @Override
    public String getRuleName() {
        return "GEO";
    }
}
