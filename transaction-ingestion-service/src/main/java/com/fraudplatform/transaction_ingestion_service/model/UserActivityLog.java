package com.fraudplatform.transaction_ingestion_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "user_activity_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "userId_timestamp", def = "{'userId': 1, 'timestamp': -1}"),
        @CompoundIndex(name = "userId_action", def = "{'userId': 1, 'action': 1}")
})
public class UserActivityLog {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String action; // TRANSACTION_CREATED, TRANSACTION_DUPLICATE etc.

    private Map<String, Object> metadata; // flexible — store anything relevant

    @Indexed
    private Instant timestamp;

    private boolean flagged;
}

