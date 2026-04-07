package com.fraudplatform.transaction_ingestion_service.repository;

import com.fraudplatform.transaction_ingestion_service.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Optional<Transaction> findByIdempotencyKey(String idempotencyKey);

    List<Transaction> findByUserId(String userId);
}
