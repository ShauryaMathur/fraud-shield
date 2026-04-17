package com.fraudplatform.user_account_service.repository;

import com.fraudplatform.user_account_service.model.beneficiary.Beneficiary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryRepository extends MongoRepository<Beneficiary, String> {
    List<Beneficiary> findAllByUserIdAndActive(String userId,  Boolean active);
}
