package com.fraudplatform.user_account_service.repository;

import com.fraudplatform.user_account_service.model.account.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends MongoRepository<Account, UUID> {
    List<Account> findAllByUserId(String userId);
    List<Account> findAllByUserIdAndActive(String userId, Boolean active);
}
