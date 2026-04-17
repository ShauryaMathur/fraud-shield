package com.fraudplatform.user_account_service.dto.accounts;

import com.fraudplatform.user_account_service.model.account.AccountType;

public record BeneficiaryDetailsDTO(
        String accountNickName,
        String beneficiaryName,
        String beneficiaryAccountNumber,
        AccountType beneficiaryAccountType
){}