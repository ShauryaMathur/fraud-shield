package com.fraudplatform.user_account_service.service;

import com.fraudplatform.user_account_service.account.AccountMapper;
import com.fraudplatform.user_account_service.dto.accounts.BeneficiaryDetailsDTO;
import com.fraudplatform.user_account_service.model.beneficiary.Beneficiary;
import com.fraudplatform.user_account_service.repository.BeneficiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final AccountMapper accountMapper;

    public List<BeneficiaryDetailsDTO>  getAllBeneficiariesForUser(String userId) {
        List<Beneficiary> beneficiaries = beneficiaryRepository.findAllByUserIdAndActive(userId, true);
        return beneficiaries.stream()
                .map(accountMapper ::toBeneficiaryDetailsDTO)
                .toList();
    }
}
