package com.fraudplatform.user_account_service.service;

import com.fraudplatform.user_account_service.dto.accounts.AccountDetailsDTO;
import com.fraudplatform.user_account_service.model.Account;
import com.fraudplatform.user_account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<AccountDetailsDTO> findAllByUserId(String userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        List<AccountDetailsDTO> accountDetailsDTOs = new ArrayList<>();
        for (Account account : accounts) {
            AccountDetailsDTO accountDetailsDTO = AccountDetailsDTO.builder()
                    .accountNumber(account.getId())
                    .accountType(account.getAccountType())
                    .balance(account.getBalance())
                    .build();
            accountDetailsDTOs.add(accountDetailsDTO);
        }
        return accountDetailsDTOs;
    }
}
