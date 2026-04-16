package com.fraudplatform.transaction_ingestion_service.repository;

import com.fraudplatform.transaction_ingestion_service.model.UserActivityLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface UserActivityLogRepository extends MongoRepository<UserActivityLog, String> {
    List<UserActivityLog> findByUserId(String userId);
    List<UserActivityLog> findByUserIdAndTimestampBetween(String userId, Instant from, Instant to);
    List<UserActivityLog> findByFlaggedTrue();
}

