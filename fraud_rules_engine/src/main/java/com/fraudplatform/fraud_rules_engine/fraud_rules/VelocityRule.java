package com.fraudplatform.fraud_rules_engine.fraud_rules;

import com.fraudplatform.fraud_rules_engine.model.TransactionEvent;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VelocityRule implements FraudRule{

    private record UserVelocityData(int count, Instant windowStart) {}

    private static final int MAX_TRANSACTIONS = 5;
    private static final int WINDOW_MINUTES = 10;

    private final Map<String, UserVelocityData> velocityMap = new ConcurrentHashMap<>();

    @Override
    public boolean evaluate(TransactionEvent event) {
        Instant now = Instant.now();
        String userId = event.getUserId();

        UserVelocityData data = velocityMap.get(userId);

        // first transaction for this user
        if (data == null) {
            velocityMap.put(userId, new UserVelocityData(1, now));
            return false;
        }

        Duration elapsed = Duration.between(data.windowStart(), now);

        // window expired — reset
        if (elapsed.toMinutes() >= WINDOW_MINUTES) {
            velocityMap.put(userId, new UserVelocityData(1, now));
            return false;
        }

        // within window — increment count
        int newCount = data.count() + 1;
        velocityMap.put(userId, new UserVelocityData(newCount, data.windowStart()));

        return newCount >= MAX_TRANSACTIONS;
    }

    @Override
    public String getRuleName() {
        return "VELOCITY_RULE";
    }
}
