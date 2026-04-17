package com.fraudplatform.user_account_service.account;

import com.fraudplatform.user_account_service.dto.accounts.AccountDetailsDTO;
import com.fraudplatform.user_account_service.dto.accounts.BeneficiaryDetailsDTO;
import com.fraudplatform.user_account_service.model.account.Account;
import com.fraudplatform.user_account_service.model.beneficiary.Beneficiary;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountDetailsDTO toAccountDetailsDTO(Account account) {
        return AccountDetailsDTO.builder()
                .accountNumber(account.getId())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .build();
    }

    public BeneficiaryDetailsDTO toBeneficiaryDetailsDTO(Beneficiary beneficiary) {
        return new BeneficiaryDetailsDTO(beneficiary.getAccountNickName(), beneficiary.getAccountHolderName(), beneficiary.getAccountNumber(), beneficiary.getAccountType());
    }
}
