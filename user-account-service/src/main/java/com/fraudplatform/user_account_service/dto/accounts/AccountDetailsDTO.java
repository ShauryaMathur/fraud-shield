package com.fraudplatform.user_account_service.dto.accounts;

import com.fraudplatform.user_account_service.model.account.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class AccountDetailsDTO {
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
}
