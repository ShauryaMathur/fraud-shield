package com.fraudplatform.user_account_service.repository.user;

import com.fraudplatform.user_account_service.model.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}
