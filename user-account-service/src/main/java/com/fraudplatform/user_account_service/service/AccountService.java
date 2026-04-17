package com.fraudplatform.user_account_service.service;

import com.fraudplatform.user_account_service.account.AccountMapper;
import com.fraudplatform.user_account_service.dto.accounts.AccountDetailsDTO;
import com.fraudplatform.user_account_service.model.account.Account;
import com.fraudplatform.user_account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public List<AccountDetailsDTO> findAllByUserId(String userId) {
        List<Account> accounts = accountRepository.findAllByUserIdAndActive(userId, true);

        return accounts.stream()
                .map(accountMapper::toAccountDetailsDTO)
                .toList();
    }
}
