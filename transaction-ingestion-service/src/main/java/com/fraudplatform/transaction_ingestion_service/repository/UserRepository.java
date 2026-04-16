package com.fraudplatform.transaction_ingestion_service.repository;

import com.fraudplatform.transaction_ingestion_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
